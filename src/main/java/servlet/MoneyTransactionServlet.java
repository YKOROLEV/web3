package servlet;

import model.BankClient;
import service.BankClientService;
import util.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MoneyTransactionServlet extends HttpServlet {

    private BankClientService service = new BankClientService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter()
                .println(PageGenerator.getInstance().getPage("moneyTransactionPage.html", Collections.emptyMap()));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> variables = new HashMap<>();

        String senderName = request.getParameter("senderName");
        String senderPass = request.getParameter("senderPass");
        long count = Long.parseLong(request.getParameter("count"));
        String nameTo = request.getParameter("nameTo");

        BankClient sender = service.getClient(senderName, senderPass);
        String resultMessage;

        if (sender != null
                && service.sendMoneyToClient(sender, nameTo, count)) {
            resultMessage = "The transaction was successful";
        } else {
            resultMessage = "transaction rejected";
        }

        variables.put("message", resultMessage);
        response.getWriter()
                .println(PageGenerator.getInstance().getPage("resultPage.html", variables));
    }
}