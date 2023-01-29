package br.com.feltex.whatsapbot.controller;

import br.com.feltex.whatsapbot.modelo.Mensagem;
import br.com.feltex.whatsapbot.modelo.Notificacao;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/zap")
@Slf4j
public class EnviaMensagem {

    @Autowired
    private WebDriver webDriver;  

    @PostMapping
    public void receberMensagem(@RequestBody Mensagem mensagem) {

        for (String contato : mensagem.getContatos()) {
            try {
                var elementoContato = findContato(contato);
                elementoContato.click();

                var caixaMensagem = findCaixaTexto();
                caixaMensagem.sendKeys(mensagem.getConteudo());
                caixaMensagem.sendKeys(Keys.RETURN);
            } catch (Exception e) {
                log.error("Não foi possível enviar a mensagem para o contato {}", contato, e);
            }
        }
    }

    @PostMapping("/notificacao")
    public void automaticaNotificacao(@RequestBody Notificacao mensagem){      
            try {           
                var elementoContato = findContatoNotificacao();
                elementoContato.click();
    
                var caixaMensagem = findCaixaTexto();
                caixaMensagem.sendKeys(mensagem.getConteudo());
                caixaMensagem.sendKeys(Keys.RETURN);
                            
            } catch (Exception e) {
                log.error("Não foi possível enviar a mensagem para o contato {}", e);              
            }          

    }

    private WebElement findContato(String nomeContato) {
        var xPathContato = "//*[@id=\"pane-side\"]/*//span[@title='" + nomeContato + "']";
       
        return webDriver.findElement(By.xpath(xPathContato));
    }
    
    private WebElement findCaixaTexto() {
        var xPathCaixaTexto = "//*[@id=\"main\"]/footer//*[@role='textbox']";
        return webDriver.findElement(By.xpath(xPathCaixaTexto));
    }

    private WebElement findContatoNotificacao() {
        String nomeContato = "icon-unread-count";
        var xPathContato = "//*[@id=\"pane-side\"]/*//span[@data-testid='" + nomeContato + "']";
       
        return webDriver.findElement(By.xpath(xPathContato));
    }
   
}
////*[@id="pane-side"]/*//span[@data-testid= "icon-unread-count"];