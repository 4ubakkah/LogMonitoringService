var logMonitorModule = angular.module('logMonitorApp', ['ngAnimate']);

logMonitorModule.controller('logMonitorController', function ($scope,$http) {

    $scope.toggle=true;
    $scope.severities=['INFO', 'WARNING', 'ERROR'];

    $scope.monitoringIntervals=[1000, 10000, 10000000];

    $scope.selectedMonitoringInterval = $scope.monitoringIntervals[0];

    $scope.monitoringInterval="";
    $scope.filePath="";

    $scope.toggle='!toggle';
    $scope.entries = [];

    $scope.monitoringStarted = false;

    setInterval(function() {
        if($scope.monitoringStarted == true)
            sendConsumeRequest();
    }, 100);

    function sendConsumeRequest() {
        $http({
            url: '/rest/monitoring/consume',
            method: 'POST',
            data : composeConsumeRequest(),
            headers : {
                'Content-Type' : 'application/json'
            }
        }).then(function successCallback(response) {
            parseConsumeResponse(response);
            fillLackingSeverities();
        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    }


    function parseConsumeResponse(consumeResponse) {
        $scope.entries = [];

        if (consumeResponse.data.statistics != null) {
            for (var key in consumeResponse.data.statistics) {
                var entry = {};
                entry.type = key;
                entry.count = consumeResponse.data.statistics[key];
                $scope.entries.push(entry);
            }
        }
    }

    function fillLackingSeverities() {
        for (var severity in $scope.severities) {
            var found = false;
            for(var entry in $scope.entries) {
                if ($scope.entries[entry].type == $scope.severities[severity]) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                var entry = {};
                entry.type = $scope.severities[severity];
                entry.count = 0;
                $scope.entries.push(entry);
            }

            found = false;
        }
    }

    function composeJsonFromUpdateConfigurationRequest() {
        var jsonObject = {};
        jsonObject.monitoringInterval = $scope.monitoringInterval;
        jsonObject.filePath = $scope.filePath;
        return JSON.stringify(jsonObject);
    }

    function composeConsumeRequest() {
        var jsonObject = {};
        jsonObject.monitoringInterval = $scope.selectedMonitoringInterval;
        return JSON.stringify(jsonObject);
    }

    function sendStartMonitoringService() {
        $http({
            url : '/rest/monitoring/start',
            method: 'POST',
            data: composeJsonFromUpdateConfigurationRequest(),
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            async: false
        });
    }

    function sendStopMonitoringService() {
        $http({
            url : '/rest/monitoring/stop',
            method: 'POST',
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            async: false
        });
    }

    $scope.updateConfiguration = function updateConfiguration() {
        if($scope.monitoringInterval==""){
            alert("Inconsistent configuration detected! Please provide value for monitoring interval");
        }
        else{
            $scope.selectedMonitoringInterval = $scope.monitoringInterval;
            $scope.toggle='!toggle';
            $scope.monitoringInterval="";
            $scope.filePath="";
        }
    };

    $scope.startMonitoring = function startMonitoring() {
        sendStartMonitoringService();
        $scope.monitoringStarted = true;
    };

    $scope.stopMonitoring = function stopMonitoring() {
        sendStopMonitoringService();
        $scope.monitoringStarted = false;
    };

});

logMonitorModule.directive('ngConfirmClick', [
    function(){
        return {
            link: function (scope, element, attr) {
                var msg = attr.ngConfirmClick || "Are you sure?";
                var clickAction = attr.confirmedClick;
                element.bind('click',function (event) {
                    if ( window.confirm(msg) ) {
                        scope.$eval(clickAction);
                    }
                });
            }
        };
    }]);