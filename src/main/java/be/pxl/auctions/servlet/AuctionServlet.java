package be.pxl.auctions.servlet;

import be.pxl.auctions.service.AuctionService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/count")
public class AuctionServlet extends HttpServlet {

	private final AuctionService auctionService;

	public AuctionServlet(AuctionService auctionService) {
		this.auctionService = auctionService;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html");
		resp.setCharacterEncoding("UTF-8");
		int numberOfAuctions = auctionService.getActiveAuctions();
		try (PrintWriter out = resp.getWriter()) {
			out.println("<html>");
			out.println("<body>");
			out.println("<h1>Er zijn momenteel " + numberOfAuctions + " veiling(en) actief.</h1>");
			out.println("</body>");
			out.println("</html>");
		}
	}
}

