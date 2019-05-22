<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ include file="/WEB-INF/template/header.jsp" %>


<openmrs:require privilege="Run Reports" otherwise="/login.htm" redirect="/module/evrreports/queuedReport.form"/>
<openmrs:htmlInclude file="/moduleResources/evrreports/js/openmrs-1.9.js"/>

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
        //reportDate.setDate(new Date());

        startDate = new DatePicker("<openmrs:datePattern/>", "dateScheduled");
        /* scheduleDate.setDate(new Date());*/

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
            <legend>Location</legend>
            <spring:bind path="queuedReports.facility.facilityId">
                <select name="${status.expression}" id="facility" size="10">
                    <c:forEach var="facility" items="${facilities}">
                        <option
                        <c:if test="${status.value==facility.facilityId}">selected</c:if> value="${facility.facilityId}">${facility.code}
                        - ${facility.name} </option>
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
