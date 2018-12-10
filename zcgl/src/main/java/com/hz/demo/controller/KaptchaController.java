package com.hz.demo.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.code.kaptcha.Producer;

@Controller
@RequestMapping("/KaptchaController")
public class KaptchaController {

    @Autowired
    private Producer producer;//生成字符串以及图片的对象

    @RequestMapping("/getKaptcha")
    public void getKaptcha(HttpSession session,HttpServletResponse response) throws IOException{

        //把带有字符串以及干扰线、干扰点的图片，响应给客户端
        //通过kaptcha工具生成字符串、生成图片
        //把图片以流的方式响应给客户端

        //生成字符串
        String text = producer.createText();

        //把text放置在session作用域中
        session.setAttribute("kaptcha", text);

        //使用生成的字符串，创建图片
        BufferedImage image = producer.createImage(text);


        //以流的方式，把图片响应到客户端
        ImageIO.write(image, "jpg", response.getOutputStream());

    }
}
