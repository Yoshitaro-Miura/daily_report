package filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import models.Employee;

/**
 * Servlet Filter implementation class LoginFilter
 */
@WebFilter("/*")
public class LoginFilter implements Filter {

    /**
     * Default constructor.
     */
    public LoginFilter() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @see Filter#destroy()
     */
    public void destroy() {
        // TODO Auto-generated method stub
    }

    /**
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // TODO Auto-generated method stub
        // place your code here

        // pass the request along the filter chain


        //daily_report_systemを取る
        String context_path = ((HttpServletRequest)request).getContextPath();

        //サーブレットパス（@XX）を取る
        String servlet_path = ((HttpServletRequest)request).getServletPath();

        if(!servlet_path.matches("/css.*")) {       // CSSフォルダ内は認証処理から除外する

            //セッションを取る
            HttpSession session = ((HttpServletRequest)request).getSession();

            // セッションスコープに保存された従業員（ログインユーザ）情報を取得
            Employee e = (Employee)session.getAttribute("login_employee");

            //パスが/loginじゃなくて
            if(!servlet_path.equals("/login")) {

                //eがnull（ログインしていないなら）
                if(e == null) {
                    //ログインページへ
                    ((HttpServletResponse)response).sendRedirect(context_path + "/login");
                    return;
                }

                //従業員管理機能にアクセスかつ、管理者フラグが0なら
                if(servlet_path.matches("/employees.*") && e.getAdmin_flag() == 0) {
                    //トップに戻す
                    ((HttpServletResponse)response).sendRedirect(context_path + "/");
                    return;
                }
                //パスがloginで
            } else {
                //eがすでに取れているときは
                if (e != null){
                    //トップへ
                    ((HttpServletResponse)response).sendRedirect(context_path + "/");
                    return;
                }
            }

        }
        chain.doFilter(request, response);
    }

    /**
     * @see Filter#init(FilterConfig)
     */
    public void init(FilterConfig fConfig) throws ServletException {
        // TODO Auto-generated method stub
    }

}
