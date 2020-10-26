package controllers.employees;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import utils.DButil;

/**
 * Servlet implementation class EmployeesIndexServlet
 */
@WebServlet("/employees/index")
public class EmployeesIndexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmployeesIndexServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        EntityManager em = DButil.createEntityManager();

        int page = 1;
        try {
            //getParameterから引いてきたページ番号を取得
            page = Integer.parseInt(request.getParameter("page"));
        } catch(NumberFormatException e) {}

            //employeesに"getAllEmployees"の結果のうち、指定されたページの開始位置から15件分、取得する
            List<Employee> employees = em.createNamedQuery("getAllEmployees",Employee.class).setFirstResult(15 * (page -1)).setMaxResults(15).getResultList();

            //employees_countに"getEmployeesCount"で取得した値を入れる
            long employees_count = (long)em.createNamedQuery("getEmployeesCount", Long.class).getSingleResult();

        em.close();

        //リクエストスコープに指定の15件と、employees_countの件数と、ページ番号を入れる
        request.setAttribute("employees", employees);
        request.setAttribute("employees_count", employees_count);
        request.setAttribute("page", page);

        //flushの値がnullじゃないなら


        if(request.getSession().getAttribute("flush") != null){
            //リクエストスコープに入れる
            request.setAttribute("flush", request.getSession().getAttribute("flush"));
            //セッションスコープからは消す
            request.getSession().removeAttribute("flush");
        }


        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/employees/index.jsp");
        rd.forward(request, response);

    }

}
