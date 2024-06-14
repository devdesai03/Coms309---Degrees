package org.springframework.samples.petclinic.system;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.NestedServletException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

@RestController
public class CustomErrorController implements ErrorController {
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        int status; {
            Integer statusObject = (Integer)request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
            if (statusObject == null) {
                status = 0;
            } else {
                status = statusObject;
            }
        }

        String message; switch (status) {
            case 0:
                message = "No error... just visiting the /error page, huh? :)";
                break;
            case 400:
                message = "Error 400: Bad request!";
                break;
            case 404:
                message = "Error 404: Not found!";
                break;
            case 500:
                message = "Error 500: Internal server error!";
                break;
            default:
                message = "Error " + status + "!";
                break;
        }
        Object exp = request.getAttribute("javax.servlet.error.exception");
        if (exp instanceof ServletException) {
            java.lang.Throwable root = ((ServletException) exp).getRootCause();
            if (root != null) exp = root;
        }
        if (exp != null)
            message += "\nCaused by:\n" + exp;
        return message;
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
