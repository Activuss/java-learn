import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ConverterServlet extends HttpServlet {
    public static final String RESPONSE_TEMPLATE = "Days in requested month: ";
    public static final String ERROR_TEMPLATE = "Unable to parse requested month. Example: ?month=july";
    public static final String MONTH_REQUEST_PARAMETER = "month";
    private Map<String, Integer> monthIds = new HashMap<>();

    {
        monthIds.put("january", 0);
        monthIds.put("february", 1);
        monthIds.put("march", 2);
        monthIds.put("april", 3);
        monthIds.put("may", 4);
        monthIds.put("june", 5);
        monthIds.put("july", 6);
        monthIds.put("august", 7);
        monthIds.put("september", 8);
        monthIds.put("october", 9);
        monthIds.put("november", 10);
        monthIds.put("december", 11);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        this.process(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        this.process(request, response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        String requestedMonth = request.getParameter(MONTH_REQUEST_PARAMETER).toLowerCase();
        if (!monthIds.containsKey(requestedMonth))
            response.getWriter().write(ERROR_TEMPLATE);
        else
            response.getWriter().write(RESPONSE_TEMPLATE + getDaysNumber(requestedMonth));
    }

    private int getDaysNumber(String monthName) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, monthIds.get(monthName));
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }
}
