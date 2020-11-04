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
@WebServlet("/reports/del_good")
public class ReportsDelGoodServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportsDelGoodServlet() {
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

        //ログインしているユーザのemployee情報を取得する
        Employee emp = (Employee) request.getSession().getAttribute("login_employee");

        //flushで出す用に、rに紐づくemployeeの名前を取る
        String emp_name = r.getEmployee().getName();

        //レポートのいいねをデクリメント
        r.setGood(r.getGood() - 1);




        Good_rep_emp gre =  em.createNamedQuery("getGoodFlg", Good_rep_emp.class).setParameter("employee", emp).setParameter("report", r).getSingleResult();





        em.getTransaction().begin();
        em.remove(gre);
        em.getTransaction().commit();
        em.close();
        request.getSession().setAttribute("flush", emp_name + "の投稿のいいね!を取り消しました。");

        request.setAttribute("report", r);
        response.sendRedirect(request.getHeader("REFERER"));
    }

}
