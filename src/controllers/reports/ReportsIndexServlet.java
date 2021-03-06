package controllers.reports;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Good_rep_emp;
import models.Report;
import utils.DButil;

/**
 * Servlet implementation class ReportsIndexServlet
 */
@WebServlet("/reports/index")
public class ReportsIndexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportsIndexServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub

        EntityManager em = DButil.createEntityManager();

        int page;
        try{
            //パラメータのpageを数値型に変換して取得
            page = Integer.parseInt(request.getParameter("page"));
            //エラーになったら（なかったら）
        } catch(Exception e){
            //1を設定
            page = 1;
        }

        //reoortsリストをつくる。全部のレポートをとってくる（★employee_idの指定は？）
        //setFirstResultは、開始位置で指定したクエリ結果だけを取得する
        //setMaxResultsは取得最大件数。
        //つまり、setFirstResultの開始位置がpageが1なら0で、そこから15件
        List<Report> reports = em.createNamedQuery("getAllReports", Report.class).setFirstResult(15 * (page -1)).setMaxResults(15).getResultList();

        long reports_count = (long)em.createNamedQuery("getReportsCount", Long.class).getSingleResult();

        //いいね判定用

        List<Good_rep_emp> gre =em.createNamedQuery("getGoodChkAllemp", Good_rep_emp.class)
                .setFirstResult(15 * (page -1))
                .setMaxResults(15)
                .getResultList();



        em.close();

        request.setAttribute("reports", reports);
        request.setAttribute("reports_count", reports_count);
        request.setAttribute("good_rep_emp", gre);

        request.setAttribute("page", page);
        if(request.getSession().getAttribute("flush") != null){
            request.setAttribute("flush", request.getSession().getAttribute("flush"));
            request.getSession().removeAttribute("flush");
        }

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/reports/index.jsp");
        rd.forward(request, response);
    }

}
