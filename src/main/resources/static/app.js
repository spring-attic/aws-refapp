/* global angular,hljs */
(function () {
    var springCloudAws = angular.module('SpringCloudAws', ['ngRoute']);

    // Global stuff
    springCloudAws.directive('active', function ($location) {
        return {
            link: function (scope, element) {
                function makeActiveIfMatchesCurrentPath() {
                    if ($location.path().indexOf(element.find('a').attr('href').substr(1)) > -1) {
                        element.addClass('active');
                    } else {
                        element.removeClass('active');
                    }
                }

                scope.$on('$routeChangeSuccess', function () {
                    makeActiveIfMatchesCurrentPath();
                });
            }
        };
    });

    springCloudAws.service('SourceCodeService', function ($http) {
        this.getSourceCode = function (path) {
            return $http.get('source?path=' + path);
        };
    });

    springCloudAws.directive('sourceCode', function (SourceCodeService, $timeout) {
        return {
            restrict: 'E',
            templateUrl: 'templates/source-box.tpl.html',
            scope: {
                path: '@'
            },
            link: function (scope, element) {
                SourceCodeService.getSourceCode(scope.path).then(function (response) {
                    scope.code = response.data.content;
                    scope.link = response.data.url;
                    scope.title = response.data.name;
                    $timeout(function () {
                        hljs.highlightBlock(element.find('code')[0]);
                    });
                });
            }
        }
    });

    // SQS
    springCloudAws.service('SqsService', function ($http) {
        this.sendMessage = function (message) {
            return $http.post('sqs/message-processing-queue', message);
        };
    });

    springCloudAws.controller('SqsCtrl', function (SqsService, $scope) {
        var self = this;
        self.model = {};
        self.responses = [];

        function initMessageToProcess() {
            self.model.messageToProcess = {
                message: undefined,
                priority: 2
            };
        }

        self.sendMessage = function () {
            SqsService.sendMessage(self.model.messageToProcess);
            initMessageToProcess();
        };

        function initView() {
            initMessageToProcess();

            var sock = new SockJS('/sqs-messages');
            sock.onmessage = function (e) {
                var jsonResponse = JSON.parse(e.data);
                self.responses.reverse().push(jsonResponse);

                if (self.responses.length > 10) {
                    self.responses = self.responses.slice(self.responses.length - 10);
                }

                self.responses = self.responses.reverse();
                $scope.$apply();
            };
        }

        initView();
    });

    springCloudAws.filter('priority', function () {
        return function (input) {
            switch (input) {
                case 1:
                    return 'Low';
                case 2:
                    return 'Medium';
                case 3:
                    return 'High';
            }
        }
    });

    // SNS
    springCloudAws.service('SnsService', function ($http) {
        this.send = function (message) {
            return $http.post('sns/send', message);
        };
    });

    springCloudAws.controller('SnsCtrl', function (SnsService, $scope) {
        var self = this;
        self.responses = [];

        function initModel() {
            self.model = {
                message: undefined,
                subject: undefined
            };
        }

        self.send = function () {
            SnsService.send(self.model);
            initModel();
        };

        function initView() {
            initModel();

            var sock = new SockJS('/sns-messages');
            sock.onmessage = function (e) {
                var jsonResponse = JSON.parse(e.data);
                self.responses.reverse().push(jsonResponse);

                if (self.responses.length > 10) {
                    self.responses = self.responses.slice(self.responses.length - 10);
                }

                self.responses = self.responses.reverse();
                $scope.$apply();
            };
        }

        initView();
    });

    // RDS
    springCloudAws.service('PersonService', function ($http) {
        this.add = function (person) {
            return $http.post('persons', person);
        };

        this.getAll = function () {
            return $http.get('persons');
        }
    });

    springCloudAws.controller('RdsCtrl', function (PersonService) {
        var self = this;
        self.persons = [];

        function refresh() {
            PersonService.getAll().then(function (response) {
                self.persons = response.data;
            });
        }

        refresh();

        function initView() {
            self.model = {
                firstName: undefined,
                lastName: undefined
            };
        }

        initView();

        self.add = function () {
            PersonService.add(self.model).then(function () {
                refresh();
            });
            initView();
        };
    });

    // ElastiCache
    springCloudAws.service('ElastiCacheService', function ($http) {
        this.getValue = function (key) {
            return $http.get('cachedService?key=' + key, {headers: {Accept: 'text/plain'}});
        };
    });

    springCloudAws.controller('ElastiCacheCtrl', function ($scope, ElastiCacheService) {
        var self = this;
        self.loading = false;

        self.getValue = function () {
            self.value = '';
            self.loading = true;
            ElastiCacheService.getValue(self.key).then(function (response) {
                self.loading = false;
                self.value = response.data;
            });
        };
    });

    // EC2
    springCloudAws.service('Ec2Service', function ($http) {
        this.getProperties = function () {
            return $http.get('instance-info');
        };
    });

    springCloudAws.controller('Ec2Ctrl', function (Ec2Service) {
        var self = this;

        Ec2Service.getProperties().then(function (response) {
            self.properties = response.data;
        });
    });

    springCloudAws.config(function ($routeProvider) {
        $routeProvider.when('/home', {templateUrl: 'pages/home.tpl.html'});
        $routeProvider.when('/sqs', {templateUrl: 'pages/sqs.tpl.html'});
        $routeProvider.when('/sns', {templateUrl: 'pages/sns.tpl.html'});
        $routeProvider.when('/elasticache', {templateUrl: 'pages/elasticache.tpl.html'});
        $routeProvider.when('/rds', {templateUrl: 'pages/rds.tpl.html'});
        $routeProvider.when('/ec2', {templateUrl: 'pages/ec2.tpl.html'});
        $routeProvider.otherwise({redirectTo: '/home'});
    });
}());