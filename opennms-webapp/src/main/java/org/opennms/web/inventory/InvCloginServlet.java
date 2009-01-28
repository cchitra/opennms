package org.opennms.web.inventory;

import java.io.IOException;
import java.text.ChoiceFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.opennms.web.inventory.InventoryLayer;

public class InvCloginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        HttpSession userSession = request.getSession(false);

        if (userSession != null) {
            String userId = request.getParameter("userID");
            String password = request.getParameter("pass");
            String loginM = request.getParameter("loginM");
            String device = request.getParameter("deviceName");
            String group = request.getParameter("groupName");

            int ret = InventoryLayer.updateCloginInfo(group, device, userId, password, loginM);

            RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher(request.getParameter("redirect"));
            dispatcher.forward(request, response);
        }
    }
        
//        private void redirect(HttpServletRequest request,
//                HttpServletResponse response) throws IOException {
//            String redirectURL = request.getHeader("Referer");
//            response.sendRedirect(redirectURL);
//        }
}
//        if (userSession != null) {
//            User newUser = (User) userSession.getAttribute("user.modifyUser.jsp");
//            try {
//                UserFactory.init();
//            } catch (Exception e) {
//                throw new ServletException("UpdateUserServlet:init Error initialising UserFactory " + e);
//            }
//            
//            // get the rest of the user information from the form
//            newUser.setFullName(request.getParameter("fullName"));
//            newUser.setUserComments(request.getParameter("userComments"));
//            newUser.setReadOnly(false);
//            if (request.getParameter("readOnly") != null && (request.getParameter("readOnly").equalsIgnoreCase("true") || request.getParameter("readOnly").equalsIgnoreCase("on"))) {
//                newUser.setReadOnly(true);
//            }
//
//            String password = request.getParameter("password");
//            if (password != null && !password.trim().equals("")) {
//                newUser.setPassword(UserFactory.getInstance().encryptedPassword(password));
//            }
//
//            String email = request.getParameter("email");
//            String pagerEmail = request.getParameter("pemail");
//            String xmppAddress = request.getParameter("xmppAddress");
//            String numericPage = request.getParameter("numericalService");
//            String numericPin = request.getParameter("numericalPin");
//            String textPage = request.getParameter("textService");
//            String textPin = request.getParameter("textPin");
//
//            newUser.removeAllContact();
//
//            Contact tmpContact = new Contact();
//            tmpContact.setInfo(email);
//            tmpContact.setType("email");
//            newUser.addContact(tmpContact);
//
//            tmpContact = new Contact();
//            tmpContact.setInfo(pagerEmail);
//            tmpContact.setType("pagerEmail");
//            newUser.addContact(tmpContact);
//
//            tmpContact = new Contact();
//            tmpContact.setInfo(xmppAddress);
//            tmpContact.setType("xmppAddress");
//            newUser.addContact(tmpContact);
//            
//            tmpContact = new Contact();
//            tmpContact.setInfo(numericPin);
//            tmpContact.setServiceProvider(numericPage);
//            tmpContact.setType("numericPage");
//            newUser.addContact(tmpContact);
//
//            tmpContact = new Contact();
//            tmpContact.setInfo(textPin);
//            tmpContact.setServiceProvider(textPage);
//            tmpContact.setType("textPage");
//            newUser.addContact(tmpContact);
//
//            // build the duty schedule data structure
//            List<Boolean> newSchedule = new ArrayList<Boolean>(7);
//            ChoiceFormat days = new ChoiceFormat("0#Mo|1#Tu|2#We|3#Th|4#Fr|5#Sa|6#Su");
//
//            Collection<String> dutySchedules = getDutySchedulesForUser(newUser);
//            dutySchedules.clear();
//
//            int dutyCount = WebSecurityUtils.safeParseInt(request.getParameter("dutySchedules"));
//            for (int duties = 0; duties < dutyCount; duties++) {
//                newSchedule.clear();
//                String deleteFlag = request.getParameter("deleteDuty" + duties);
//                // don't save any duties that were marked for deletion
//                if (deleteFlag == null) {
//                    for (int i = 0; i < 7; i++) {
//                        String curDayFlag = request.getParameter("duty" + duties + days.format(i));
//                        newSchedule.add(new Boolean(curDayFlag != null));
//                    }
//
//                    int startTime = WebSecurityUtils.safeParseInt(request.getParameter("duty" + duties + "Begin"));
//                    int stopTime = WebSecurityUtils.safeParseInt(request.getParameter("duty" + duties + "End"));
//
//                    DutySchedule newDuty = new DutySchedule(newSchedule, startTime, stopTime);
//                    dutySchedules.add(newDuty.toString());
//                }
//            }
//
//            userSession.setAttribute("user.modifyUser.jsp", newUser);
//        }
//
//        // forward the request for proper display
//        RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher(request.getParameter("redirect"));
//        dispatcher.forward(request, response);
//    }
//
//    private List<String> getDutySchedulesForUser(User newUser) {
//        return newUser.getDutyScheduleCollection();
