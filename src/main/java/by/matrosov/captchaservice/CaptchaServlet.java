package by.matrosov.captchaservice;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

@WebServlet("/CaptchaService")
public class CaptchaServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        int iHeight = 40;
        int iWidth = 150;
        int iTotalChars = 6;

        Font fntStyle = new Font("Arial", Font.BOLD, 30);

        Random randChars = new Random();
        String sImageCode = Captcha.generateCaptcha3(iTotalChars);

        BufferedImage biImage = new BufferedImage(iWidth, iHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2dImage = (Graphics2D) biImage.getGraphics();

        g2dImage.setFont(fntStyle);
        for (int i = 0; i < iTotalChars; i++) {
            g2dImage.setColor(new Color(randChars.nextInt(255), randChars.nextInt(255), randChars.nextInt(255)));
            if (i % 2 == 0){
                g2dImage.drawString(sImageCode.substring(i, i+1), 25 * i, 24);
            }else {
                g2dImage.drawString(sImageCode.substring(i, i+1), 25 * i, 35);
            }
        }

        //off cache
        ImageIO.setUseCache(false);

        // Set appropriate http headers
        resp.setContentType( "image/jpeg");
        resp.setHeader("Request ID", req.getRequestedSessionId());
        resp.setHeader("Verified Captcha", sImageCode);

        req.getSession().setAttribute("captchaSecurity", sImageCode);
        req.getSession().setAttribute("reqId", req.getRequestedSessionId());

        // Write the image to the client
        OutputStream osImage = resp.getOutputStream();
        ImageIO.write(biImage, "jpeg", osImage);
        g2dImage.dispose();

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String firstParam = req.getParameter("cap");
        String secondParam = req.getParameter("id");

        if (firstParam == null || secondParam == null){
            doGet(req,resp);
        }else {
            if (firstParam.equals(req.getSession().getAttribute("captchaSecurity")) &&
                    secondParam.equals(req.getSession().getAttribute("reqId"))){
                resp.setStatus(202);
            }else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Captcha or Id is wrong!");
            }
        }
    }
}
