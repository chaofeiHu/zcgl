package com.hz.demo.controller;

import com.hz.demo.core.PageData;
import com.hz.demo.model.rec_result;
import com.hz.demo.services.RecResultService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/***
 * 通过二维码查看证书文件
 */
@Controller
@RequestMapping("/PreviewCertificatePdf")
public class PreviewCertificatePdfController extends BaseController {

    @Autowired
    RecResultService recResultService;

    @RequestMapping(value = "/getCertificate/{certificateNumber}/{idCardNo}", method = RequestMethod.GET)
    public String getCertificate(@PathVariable("certificateNumber") String certificateNumber, @PathVariable("idCardNo") String idCardNo,HttpServletRequest request, HttpServletResponse response) {
        PageData pd=new PageData();
        pd.put("certificateNumber",certificateNumber);
        pd.put("idCardNo",idCardNo);
        rec_result recResult = recResultService.selectResultCertificateByIdCardAndCertificateNumber(pd);
        if (recResult!=null&& !StringUtils.isEmpty(recResult.getRecCertificate().getCertificatePath())) {
            System.out.println("证书存在");
            request.setAttribute("certificatePath", recResult.getRecCertificate().getCertificatePath());
        }else{
            System.out.println("证书不存在");
            request.setAttribute("certificatePath", "");
        }
        return "sys/getCertificateByCode";
    }


}
