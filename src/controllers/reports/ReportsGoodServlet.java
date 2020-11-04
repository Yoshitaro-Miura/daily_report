package controllers.reports;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.Good_rep_emp;
import models.Report;
import utils.DButil;

/**
 * Servlet implementation class ReportsGoodServlet
 */
@WebServlet("/reports/good")
public class ReportsGoodServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportsGoodServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub

        EntityManager em = DButil.createEntityManager();


        Report r = em.find(Report.class, Integer.parseInt(request.getParameter("id")));

        //flushに出す用に誰のレポートをいじったかを格納
        String emp_name = r.getEmployee().getName();

        //レポートのいいねをインクリメント
        r.setGood(r.getGood() + 1);

        //いいね履歴に書き込み
        Good_rep_emp gre = new Good_rep_emp();

        //いじった人を探すために、ログインしているemployeeの情報を取る
        Employee emp = (Employee) request.getSession().getAttribute("login_employee");

        //いじった人がだれかを入れる
        gre.setEmployee(emp);
        gre.setGood_flg(1);
        gre.setReport(r);

        em.getTransaction().begin();
        em.persist(gre);
        em.getTransaction().commit();
        em.close();
        request.getSession().setAttribute("flush", emp_name + "の投稿にいいね!しました。");

        request.setAttribute("report", r);
        response.sendRedirect(request.getHeader("REFERER"));
    }

}
