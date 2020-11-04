<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">
        <c:if test="${flush != null}">
            <div id="flush_success">
                <c:out value="${flush}"></c:out>
            </div>
        </c:if>
        <h2>日報　一覧</h2>
        ${sessionScope.login_employee.id}
        <table id="report_list">
            <tbody>
                <tr>
                    <th class="report_name">氏名</th>
                    <th class="report_date">日付</th>
                    <th class="report_title">タイトル</th>
                    <th class="report_action">操作</th>
                    <th class="report_good">いいね</th>
                </tr>

                <c:forEach var="report" items="${reports}" varStatus="status">
                    <tr class="row${status.count % 2}">
                    <!-- report.employee.nameの書き方 -->
                        <td class="report_name"><c:out value="${report.employee.name}" /></td>
                        <td class="report_date"><fmt:formatDate value='${report.report_date}' pattern='yyyy-MM-dd' /></td>
                        <td class="report_title">${report.title}</td>
                        <td class="report_action"><a href="<c:url value='/reports/show?id=${report.id}' />">詳細を見る</a></td>

                        <c:choose>
                            <%-- good_rep_empがNULLのとき --%>
                            <c:when test = "${empty good_rep_emp}">
                                <td class="rel_good"><a href="<c:url value='/reports/good?id=${report.id}' />">いいね!</a> (<c:out value="${report.good}" />)</td>
                            </c:when>

                            <%-- good_rep_empが何等かあるとき --%>
                            <c:otherwise>
                                <%-- flg変数の宣言 --%>
                                <c:set var="flgchk" value="0" />

                                <%-- good_rep_empのリストを全部開く --%>
                                <c:forEach var="gre" items="${good_rep_emp}">

                                        <%-- 大ループのreport_idとgood_rep_empのidが一致していた場合 --%>
                                        <c:if test = "${report.id == gre.report.id}">

                                            <%--かつgood_rep_empのgood_flgが1のとき  --%>
                                              <c:if test = "${gre.good_flg == 1}">

                                              <%--かつログインしているemployeeのIDとreportにいいねしたひとのIDが一致しているとき --%>
                                                    <c:if test = "${sessionScope.login_employee.id == gre.employee.id}">

                                                    <%-- flg変数を1にする --%>
                                                    <c:set var="flgchk" value="1" />

                                                    </c:if>
                                              </c:if>
                                        </c:if>

                                        <%-- 大ループのreport.idとgood_rep_empのidが一致していない場合は何もしない（flg変数は0のまま） --%>
                                </c:forEach>

                                <c:choose>
                                    <%-- flg変数が1のとき(大ループと小ループのrep.idが一致しているかつ、good_flgは1)はいいねを取り消すを表示 --%>
                                    <c:when test = "${flgchk == 1}">
                                        <td class="rel_good"><a href="<c:url value='/reports/del_good?id=${report.id}' />">いいね!を取り消す</a> (<c:out value="${report.good}" />)</td>
                                    </c:when>
                                    <c:otherwise>
                                    <%-- flg変数が0のときはいいねを取り消すを表示 --%>
                                        <td class="rel_good"><a href="<c:url value='/reports/good?id=${report.id}' />">いいね!</a> (<c:out value="${report.good}" />)</td>
                                    </c:otherwise>
                                </c:choose>
                            </c:otherwise>
                        </c:choose>

                    </tr>
                </c:forEach>
            </tbody>
         </table>

         <div id="pagination">
            （全 ${reports_count} 件）<br />
            <c:forEach var="i" begin="1" end="${((reports_count - 1) / 15) + 1}" step="1">
                <c:choose>
                    <c:when test="${i == page}">
                        <c:out value="${i}" />&nbsp;
                    </c:when>
                    <c:otherwise>
                        <a href="<c:url value='/reports/index?page=${i}' />"><c:out value="${i}" /></a>&nbsp;
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>
        <p><a href="<c:url value='/reports/new' />">新規日報の登録</a></p>

    </c:param>
</c:import>
