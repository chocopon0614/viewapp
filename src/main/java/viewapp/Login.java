package viewapp;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import viewapp.entity.Userinfo;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		String UserName = request.getParameter("username");
		String PassWord = request.getParameter("password");
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("ViewApp");
		EntityManager em = emf.createEntityManager();
		
		List<Userinfo> UserObj = em.createNamedQuery("Userinfo.findbyusername",Userinfo.class)
				.setParameter(1, UserName)
				.getResultList();
		
		if (!(UserObj == null || UserObj.size() == 0)) {
			String DbPass = UserObj.get(0).getPassword();
			if (PassWord.equals(DbPass)) {

				request.setAttribute("UserName", UserName);
				this.getServletContext().getRequestDispatcher("/Menu.jsp").forward(request, response);
				
			} else {
				request.setAttribute("ErrorMes", "Password Error");
				this.getServletContext().getRequestDispatcher("/Login.jsp").forward(request, response);
			}
				
		}else {
			request.setAttribute("ErrorMes", "Username Error");
			this.getServletContext().getRequestDispatcher("/Login.jsp").forward(request, response);
		}
	}

}
