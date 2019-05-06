<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ include file="/WEB-INF/template/header.jsp" %>

<openmrs:require privilege="View Global Properties" otherwise="/login.htm" redirect="/module/evrreports/settings.form"/>

<%@ include file="localHeader.jsp" %>

<openmrs:htmlInclude file="/dwr/interface/DWRAdministrationService.js" />

<style>
    span.saved { font-style: italic; color: green; display: none; }
    div#taskMessage { font-style: italic; }
    div#choices, div#buttons { margin-top: 1em; }
    b.boxHeader { padding-left: 0.5em; }
</style>

<script>

    function enableSaveFor(wrapper) {
        $j("#" + wrapper + " input[name=save]").fadeIn();
    }

    function saveGPFor(wrapper) {
        var property = $j("#" + wrapper + " [name=property]").val();
        var value = $j("#" + wrapper + " [name=value]").val();

        DWRAdministrationService.setGlobalProperty(property, value, function(){
            $j("#" + wrapper + " input[name=save]").fadeOut("fast", function(){
                $j("#" + wrapper + " span.saved").fadeIn("fast", function(){
                    $j("#" + wrapper + " span.saved").fadeOut(2000);
                });
            });
        });

        return false;
    }

    function clearTaskMessage() {
        $j("#taskMessage").fadeOut("fast", function(){
            $j("#taskMessage span.message").html("");
        });
    }

    function showTaskMessage(message) {
        $j("#taskMessage span.message").html(message);
        $j("#taskMessage").fadeIn();
    }

    function getTaskRunnerStatus() {
        DWRAmrsReportService.getTaskRunnerStatus(function(result){
            if (!result)
                clearTaskMessage();
            else
                showTaskMessage(result);
        });
    }

    $j(document).ready(function() {

        // hide save buttons
        $j("input[name=save]").hide();

        // hide task message
        $j("#taskMessage").hide();

        // action event for start button
        $j("#startTask").click(function(event){
            event.preventDefault();

            var taskName = $j("input[name=taskName]:checked").val();
            if (taskName === "")
                return;

            DWRAmrsReportService.startTaskRunner(taskName, function(result) {
                showTaskMessage(result);
                $j("input[name=taskName]:checked").removeAttr("checked");
            });
        });

        // action event for stop button
        $j("#stopTask").click(function(event){
            event.preventDefault();

            DWRAmrsReportService.stopTaskRunner(function(result){
                if (!result)
                    clearTaskMessage();
                else
                    showTaskMessage(result);
            });
        });

        // get the latest status to pre-populate the page
        getTaskRunnerStatus();

        // schedule the task runner status to update every 5 seconds
        setInterval(getTaskRunnerStatus, 5000);
    });

</script>

<h2>Manage ETL Process</h2>


<br />

<b class="boxHeader">Refresh ETL Tables</b>
<div class="box" style="width:99%; height:auto; overflow-x:auto; padding: 0.75em 0.5em;">

    <form>
        <div id="taskMessage">
            <span class="message"></span>
            <!--
            <button id="stopThread">Stop Running Task</button>
            -->
        </div>

        <div id="choices">
            <div class="choice"><input type="radio" name="taskName" value="enrollment"/> </div>
        </div>

        <div id="buttons">
            <button id="startTask">Start Task</button>
        </div>
    </form>

</div>

<%@ include file="/WEB-INF/template/footer.jsp" %>
