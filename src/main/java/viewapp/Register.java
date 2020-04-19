package viewapp;

import java.io.IOException;
import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import viewapp.entity.Userinfo;

/**
 * Servlet implementation class Register
 */
@WebServlet("/Register")
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Register() {
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
		String UserName = request.getParameter("reg-name");
		String PassWord = request.getParameter("reg-pass");
		
	    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ViewApp");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = null;
		
		try {
	      tx = em.getTransaction();
	      tx.begin();

	      Userinfo userinfo = new Userinfo();
	      userinfo.setUsername(UserName);
	      userinfo.setPassword(PassWord);
	      userinfo.setCreateTime(new Timestamp(System.currentTimeMillis()));
	      userinfo.setModifiedTime(new Timestamp(System.currentTimeMillis()));

	      em.persist(userinfo);
	    
	      tx.commit();
		  request.setAttribute("ResMessage", "Create a new user account!");
		
		}catch (RuntimeException e) {
			if ( tx != null && tx.isActive() ) tx.rollback();
		    request.setAttribute("ResMessage", "An error occured! Please check your input.");

		} finally {
			em.close();
		}
		
		this.getServletContext().getRequestDispatcher("/Login.jsp").forward(request, response);
		
	}

}
