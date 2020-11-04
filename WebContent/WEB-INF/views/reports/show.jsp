<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">
        <c:choose>
            <c:when test="${report != null}">
                <h2>日報　詳細ページ</h2>

                <table>
                    <tbody>
                        <tr>
                            <th>氏名</th>
                            <td><c:out value="${report.employee.name}" /></td>
                        </tr>
                        <tr>
                            <th>日付</th>
                            <td><fmt:formatDate value="${report.report_date}" pattern="yyyy-MM-dd" /></td>
                        </tr>
                        <tr>
                            <th>内容</th>
                            <td>
                                <pre><c:out value="${report.content}" /></pre>
                            </td>
                        </tr>
                        <tr>
                            <th>登録日時</th>
                            <td>
                                <fmt:formatDate value="${report.created_at}" pattern="yyyy-MM-dd HH:mm:ss" />
                            </td>
                        </tr>
                        <tr>
                            <th>更新日時</th>
                            <td>
                                <fmt:formatDate value="${report.updated_at}" pattern="yyyy-MM-dd HH:mm:ss" />
                            </td>
                        </tr>
                    </tbody>
                </table>

                <br/>

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

                        <br/>
                        <table>
                        <tbody>
                        <tr>
                        <img src="../img/iine.png"/ width="30" height="30">
                            <c:forEach var="gre" items="${good_rep_emp}">
                                <c:out value="${gre.employee.name}" />
                            </c:forEach>
                        </tr>
                        </tbody>
                        </table>


                <br/>
                <c:if test="${sessionScope.login_employee.id == report.employee.id}">
                    <p><a href="<c:url value="/reports/edit?id=${report.id}" />">この日報を編集する</a></p>
                </c:if>

            </c:when>
            <c:otherwise>
                <h2>お探しのデータは見つかりませんでした。</h2>
            </c:otherwise>
        </c:choose>

        <p><a href="<c:url value="/reports/index" />">一覧に戻る</a></p>
    </c:param>
</c:import>