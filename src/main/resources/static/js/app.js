var taskManagerModule = angular.module('taskManagerApp', ['ngAnimate']);

taskManagerModule.controller('taskManagerController', function ($scope,$http) {

	$scope.toggle=true;
	$scope.severities=['INFO', 'WARNING', 'ERROR',];

    $scope.monitoringIntervals=[1000, 10000, 10000000];

    $scope.monitoringInterval="";
    $scope.filePath="";

    $scope.toggle='!toggle';
    $scope.entries = [];

    $scope.monitoringStarted = false;

    setInterval(function() {
        if($scope.monitoringStarted == true)
            sendAjaxConsumeRequest();
    }, 5000);

    function sendAjaxConsumeRequest() {
        var consumeRequest = JSON.stringify({});
        $.ajax({
            url: '/rest/monitoring/consume',
            type: 'post',
            data: consumeRequest,
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            async: false,
            success: function (consumeResponse) {
                parseConsumeResponse(consumeResponse);
                fillLackingSeverities();
                $scope.$apply();
            }
        });
    }

    function parseConsumeResponse(consumeResponse) {
        $scope.entries = [];

        if (Object.keys(consumeResponse.statistics).length != 0) {
            for (var key in consumeResponse.statistics) {
                var entry = {};
                entry.type = key;
                entry.count = consumeResponse.statistics[key];
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


    function sendAjaxUpdateConfigurationRequest() {
        $.ajax({
            url : '/rest/monitoring/configure',
            type : 'post',
            data :  composeJsonFromUpdateConfigurationRequest(),
            contentType: 'application/json; charset=utf-8',
            dataType : 'json',
            async : false
        });
    }

    function composeJsonFromUpdateConfigurationRequest() {
        var jsonObject = {};
        jsonObject.monitoringInterval = $scope.monitoringInterval;
        jsonObject.filePath = $scope.filePath;
        return JSON.stringify(jsonObject);
    }

    function sendAjaxStartMonitoringService() {
        $.ajax({
            url : '/rest/monitoring/start',
            type : 'post',
            contentType: 'application/json; charset=utf-8',
            dataType : 'json',
            data :  composeJsonFromUpdateConfigurationRequest(),
            async : false
        });
    }

    function sendAjaxStopMonitoringService() {
        $.ajax({
            url : '/rest/monitoring/stop',
            type : 'post',
            contentType: 'application/json; charset=utf-8',
            dataType : 'json',
            async : false
        });
    }

    $scope.updateConfiguration = function updateConfiguration() {
        if($scope.monitoringInterval=="" || $scope.filePath=="" || $scope.taskPriority == ""){
            alert("Inconsistent configuration detected! Please provide values for monitoring interval, file path, priority");
        }
        else{
            sendAjaxUpdateConfigurationRequest();

            $scope.toggle='!toggle';
            $scope.monitoringInterval="";
            $scope.filePath="";
        }
    };

    $scope.startMonitoring = function startMonitoring() {
        sendAjaxStartMonitoringService();
        $scope.monitoringStarted = true;
        $scope.$apply();
    };

    $scope.stopMonitoring = function stopMonitoring() {
        sendAjaxStopMonitoringService();
        $scope.monitoringStarted = false;
        $scope.$apply();
    };
	
});

//Angularjs Directive for confirm dialog box
taskManagerModule.directive('ngConfirmClick', [
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