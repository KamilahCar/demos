package com.revature.handler;

import com.revature.dao.ReimbursementDao;
import com.revature.dao.ReimbursementDaoImpl;
import com.revature.model.Reimbursement;
import com.revature.model.ReimbursementForm;
import com.revature.model.ReimbursementStatus;
import com.revature.util.Json;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author William Gentry
 */
public class ReimbursementHandler {

	private static final ReimbursementDao dao = ReimbursementDaoImpl.getInstance();
	private static final Logger logger = LogManager.getLogger(ReimbursementHandler.class);

	public static void handleGetAllReimbursementsRequest(HttpServletRequest request, HttpServletResponse response) {
		List<Reimbursement> reimbursements = dao.findAll();
		if (reimbursements.isEmpty()) {
			response.setStatus(HttpServletResponse.SC_NO_CONTENT);
			return;
		} else {
			response.setContentType(Json.CONTENT_TYPE);
			try {
				response.getOutputStream().write(Json.write(reimbursements));
				return;
			} catch (IOException e) {
				logger.warn("Failed to write to Response Body: {}", e);
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				return;
			}
		}
	}

	public static void handleCreateReimbursementRequest(HttpServletRequest request, HttpServletResponse response) {
		// Attempt to Marshall the request body into a com.revature.model.ReimbursementForm
		try {
			ReimbursementForm form = (ReimbursementForm) Json.read(request.getInputStream(), ReimbursementForm.class);
			boolean wasSuccessful = dao.create(form);
			if (wasSuccessful) {
				response.setStatus(HttpServletResponse.SC_CREATED);
				return;
			} else {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				return;
			}
		} catch (IOException e) {
			logger.warn("Failed to read to Request Body: {}", e);
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
	}

	public static void handleReimbursementResolution(HttpServletRequest request, HttpServletResponse response) {
		try {
			Reimbursement reimbursement = (Reimbursement) Json.read(request.getInputStream(), Reimbursement.class);
			Reimbursement resolved = dao.resolve(reimbursement, reimbursement.getStatus());
			if (resolved != null) {
				// We return the updated reimbursement so that the client can replace it in table
				// Without having to get all reimbursements
				response.getOutputStream().write(Json.write(resolved));
				return;
			} else {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				return;
			}
		} catch (IOException e) {
			logger.warn("Failed to write to Response Body: {}", e);
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
	}

	public static void handleGetOneReimbursement(HttpServletRequest request, HttpServletResponse response) {
		String[] path = request.getRequestURI().split("/");
		int reimbursementId = Integer.parseInt(path[path.length - 1]);
		Reimbursement reimbursement = dao.findOne(reimbursementId);
		if (reimbursement != null) {
			try {
				response.setContentType(Json.CONTENT_TYPE);
				response.getOutputStream().write(Json.write(reimbursement));
			} catch(IOException e) {
				logger.warn("Failed to write to Response Body: {}", e);
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				return;
			}
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
	}

	public static void handleGetReimbursementsByStatus(HttpServletRequest request, HttpServletResponse response) {
		String status = request.getParameter("status");
		try {
			response.setContentType(Json.CONTENT_TYPE);
			switch (status) {
				case "PENDING":
					response.getOutputStream().write(Json.write(dao.findAll(ReimbursementStatus.PENDING)));
					return;
				case "APPROVED":
					response.getOutputStream().write(Json.write(dao.findAll(ReimbursementStatus.APPROVED)));
					return;
				case "REJECTED":
					response.getOutputStream().write(Json.write(dao.findAll(ReimbursementStatus.REJECTED)));
					return;
				default:
					return;
			}
		} catch (IOException e) {
			logger.warn("Failed to write to Response Body: {}", e);
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
	}

	public static void handleGetReimbursementByUsername(HttpServletRequest request, HttpServletResponse response) {
		String username = request.getParameter("username");
		List<Reimbursement> reimbursements = dao.findAll(username);
		if (reimbursements.isEmpty()) {
			response.setStatus(HttpServletResponse.SC_NO_CONTENT);
			return;
		} else {
			try {
				response.getOutputStream().write(Json.write(reimbursements));
				return;
			} catch (IOException e) {
				logger.warn("Failed to write to Response Body: {}", e);
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				return;
			}
		}
	}
}
