package controllers.employees;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.validators.EmployeeValidator;
import utils.DButil;
import utils.EncryptUtil;

/**
 * Servlet implementation class EmployeeUpdateServlet
 */
@WebServlet("/employee/update")
public class EmployeeUpdateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmployeeUpdateServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub

        String _token = (String)request.getParameter("_token");
        if (_token != null && _token.equals(request.getSession().getId())){
            EntityManager em = DButil.createEntityManager();

            //このgetSession()⇒idはeditで詰めたものを利用
            Employee e = em.find(Employee.class, (Integer)(request.getSession().getAttribute("id")));

            //社員番号が違っていたら重複チェックを行うflagを立てる
            Boolean code_duplicate_check = true;

            //送られてきたものと、サーバ側で持っているものが同じなら、
            if(e.getCode().equals(request.getParameter("code"))){
                //false
                code_duplicate_check = false;
            } else {
                //違うならparameterをセットする
                e.setCode(request.getParameter("code"));
            }

            //パスワードが違っていたらパスワード入力チェックを行うflagを立てる
            Boolean password_check_flag = true;
            String password = request.getParameter("password");

            //nullか未入力なら
            if(password == null || password == ("")){
                //false
                password_check_flag = false;
            } else {
                //そうじゃないなら設定。ここの第二パラメータ何？
                e.setPassword(EncryptUtil.getPasswordEncrypt(password, (String)this.getServletContext().getAttribute("pepper")));
            }

            e.setName(request.getParameter("name"));
            e.setAdmin_flag(Integer.parseInt(request.getParameter("admin_flag")));
            e.setUpdated_at(new Timestamp(System.currentTimeMillis()));
            e.setDelete_flag(0);

            //validateを呼ぶ
            List<String> errors = EmployeeValidator.validate(e, code_duplicate_check, password_check_flag);
                if (errors.size() > 0){
                    //もう一回セッションIDの詰めなおして、jspのhiddenに入れる
                    request.setAttribute("_token", request.getSession().getId());
                    request.setAttribute("employee", e);
                    request.setAttribute("errors", errors);

                    em.close();

                    RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/employees/edit.jsp");
                    rd.forward(request, response);

                } else {
                    em.getTransaction().begin();
                    em.getTransaction().commit();
                    em.close();
                }

                request.getSession().setAttribute("flush", "更新が完了しました。");
                request.getSession().removeAttribute("employee_id");
                response.sendRedirect(request.getContextPath() + "/employees/index");
        }

    }


}
