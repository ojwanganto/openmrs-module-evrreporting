<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ include file="/WEB-INF/template/header.jsp" %>


<openmrs:require privilege="Run Reports" otherwise="/login.htm" redirect="/module/evrreports/queuedReport.form"/>

<%@ include file="localHeader.jsp" %>


<c:if test="${not empty queuedReports.queuedReportId}">
    <h2>Edit Scheduled Report</h2>
</c:if>

<c:if test="${empty queuedReports.queuedReportId}">
    <h2>Add Scheduled Report</h2>
</c:if>

<style>
    fieldset.visualPadding {
        padding: 1em;
    }

    .right {
        text-align: right;
    }

    input.hasDatepicker {
        width: 14em;
    }
</style>

<script type="text/javascript">

    var reportDate;
    var startDate;

    $j(document).ready(function () {

        reportDate = new DatePicker("<openmrs:datePattern/>", "evaluationDate");
        startDate = new DatePicker("<openmrs:datePattern/>", "dateScheduled");

        // -----------------------------------------------

            // set the trigger for drop downs
            $j("#county").live("change", function(event){
                event.preventDefault();
                var selVal = $j(this).val();

                // Get list of wards
                if (selVal != null && selVal !="") {
                    DWRAmrsReportService.getChildrenForLocation(selVal, function (locations) {
                        var listitems = '<option></option>';
                        var subCountySelect = $j("#subCounty");
                        var wardSelect = $j("#ward");
                        var facilitySelect = $j("#facility");
                        subCountySelect.find('option').remove();
                        wardSelect.find('option').remove();
                        facilitySelect.find('option').remove();
                        $j.each(locations, function(key, value){
                            listitems += '<option value=' + key + '>' + value + '</option>';
                        });
                        subCountySelect.append(listitems);
                    });
                }
            });

            $j("#subCounty").live("change", function(event){
                event.preventDefault();
                var selVal = $j(this).val();

                // Get list of wards
                if (selVal != null && selVal !="") {
                    DWRAmrsReportService.getChildrenForLocation(selVal, function (locations) {
                        var listitems = '<option></option>';
                        var wardSelect = $j("#ward");
                        var facilitySelect = $j("#facility");
                        wardSelect.find('option').remove();
                        facilitySelect.find('option').remove();
                        $j.each(locations, function(key, value){
                            listitems += '<option value=' + key + '>' + value + '</option>';
                        });
                        wardSelect.append(listitems);
                    });
                }
            });

        $j("#ward").live("change", function(event){
            event.preventDefault();
            var selWard = $j(this).val();

            // Get list of facilities
            if (selWard != null && selWard !="") {
                DWRAmrsReportService.getChildrenForLocation(selWard, function (locations) {
                    var listitems = '<option></option>';
                    var facilitySelect = $j("#facility");
                    facilitySelect.find('option').remove();
                    $j.each(locations, function(key, value){
                        listitems += '<option value=' + key + '>' + value + '</option>';
                    });
                    facilitySelect.append(listitems);
                });
            }
        });
        // -----------------------------------------------

    });

</script>


<b class="boxHeader">Report Details</b>

<div class="box" style=" width:99%; height:auto;  overflow-x: auto;">

    <spring:hasBindErrors name="queuedReports">
        <spring:message code="fix.error"/>
        <br/>
    </spring:hasBindErrors>

    <form method="POST">
        <fieldset class="visualPadding">
            <legend>Reporting Dates</legend>
            <table cellspacing="0" cellpadding="2">
                <tr>
                    <td class="right">
                        <label for="dateScheduled">Start Date:</label>
                    </td>
                    <td>
                        <spring:bind path="queuedReports.dateScheduled">
                            <input type="text" id="dateScheduled" name="${status.expression}" value="${status.value}"/>
                            <c:if test="${status.error}">
                                Error codes:
                                <c:forEach items="${status.errorMessages}" var="error">
                                    <c:out value="${error}"/>
                                </c:forEach>
                            </c:if>
                        </spring:bind>
                    </td>
                </tr>
                <tr><td></td></tr>
                <tr>
                    <td class="right">
                        <label for="evaluationDate">End Date:</label>
                    </td>
                    <td>
                        <spring:bind path="queuedReports.evaluationDate">
                            <input type="text" id="evaluationDate" name="${status.expression}" value="${status.value}"/>
                            <c:if test="${status.error}">
                                Error codes:
                                <c:forEach items="${status.errorMessages}" var="error">
                                    <c:out value="${error}"/>
                                </c:forEach>
                            </c:if>
                        </spring:bind>
                    </td>
                </tr>
            </table>
        </fieldset>

        <fieldset class="visualPadding">
            <legend>County</legend>
            <spring:bind path="queuedReports.county">
                <select name="${status.expression}" id="county">
                    <option></option>
                    <c:forEach var="county" items="${counties}">
                        <option <c:if test="${status.value==county.locationId}">selected</c:if> value="${county.locationId}">${county.name} </option>
                    </c:forEach>
                </select>
                <c:if test="${status.error}">
                    Error codes:
                    <c:forEach items="${status.errorMessages}" var="error">
                        <c:out value="${error}"/>
                    </c:forEach>
                </c:if>
            </spring:bind>
        </fieldset>
        <fieldset class="visualPadding">
            <legend>Sub County</legend>
            <spring:bind path="queuedReports.subCounty">
                <select name="${status.expression}" id="subCounty">
                    <option></option>
                    <%--<c:forEach var="subCounty" items="${subcounties}">
                        <option <c:if test="${status.value==subCounty.locationId}">selected</c:if> value="${subCounty.locationId}">${subCounty.name}</option>
                    </c:forEach>--%>
                </select>
                <c:if test="${status.error}">
                    Error codes:
                    <c:forEach items="${status.errorMessages}" var="error">
                        <c:out value="${error}"/>
                    </c:forEach>
                </c:if>
            </spring:bind>
        </fieldset>
        <fieldset class="visualPadding">
            <legend>Ward</legend>
            <spring:bind path="queuedReports.ward">
                <select name="${status.expression}" id="ward">
                    <option></option>
                    <%--<c:forEach var="ward" items="${wards}">
                        <option <c:if test="${status.value==ward.locationId}">selected</c:if> value="${ward.locationId}">${ward.name}</option>
                    </c:forEach>--%>
                </select>
                <c:if test="${status.error}">
                    Error codes:
                    <c:forEach items="${status.errorMessages}" var="error">
                        <c:out value="${error}"/>
                    </c:forEach>
                </c:if>
            </spring:bind>
        </fieldset>

        <fieldset class="visualPadding">
            <legend>Facility</legend>
            <spring:bind path="queuedReports.facility">
                <select name="${status.expression}" id="facility">
                    <option></option>
                    <%--<c:forEach var="facility" items="${facilities}">
                        <option <c:if test="${status.value==facility.locationId}">selected</c:if> value="${facility.locationId}">${facility.name}</option>
                    </c:forEach>--%>
                </select>
                <c:if test="${status.error}">
                    Error codes:
                    <c:forEach items="${status.errorMessages}" var="error">
                        <c:out value="${error}"/>
                    </c:forEach>
                </c:if>
            </spring:bind>
        </fieldset>

        <fieldset class="visualPadding">
            <legend>Reports</legend>
            <spring:bind path="queuedReports.reportName">
                <c:forEach var="report" items="${reportProviders}">
                    <div class="reportProvider <c:if test="${not report.visible}"> hidden</c:if>">
                        <input type="radio" name="reportName"
                               <c:if test="${status.value==report.name}">checked</c:if>
                               value="${report.name}" /> ${report.name}
                    </div>
                </c:forEach>
                <c:if test="${status.error}">
                    Error codes:
                    <c:forEach items="${status.errorMessages}" var="error">
                        <c:out value="${error}"/>
                    </c:forEach>
                </c:if>
            </spring:bind>
        </fieldset>

        <input id="submitButton" class="visualPadding newline" type="submit" value="Queue for processing"/>

    </form>

</div>

<%@ include file="/WEB-INF/template/footer.jsp" %>
