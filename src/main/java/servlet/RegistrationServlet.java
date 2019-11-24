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

public class RegistrationServlet extends HttpServlet {

    private BankClientService service = new BankClientService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter()
                .println(PageGenerator.getInstance().getPage("registrationPage.html", Collections.emptyMap()));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> variables = new HashMap<>();

        String name = request.getParameter("name");
        String password = request.getParameter("password");
        long money = Long.parseLong(request.getParameter("money"));

        BankClient client = new BankClient(name, password, money);
        String resultMessage;

        if (service.addClient(client)) {
            resultMessage = "Add client successful";
        } else {
            resultMessage = "Client not add";
        }

        variables.put("message", resultMessage);
        response.getWriter()
                .println(PageGenerator.getInstance().getPage("resultPage.html", variables));
    }
}