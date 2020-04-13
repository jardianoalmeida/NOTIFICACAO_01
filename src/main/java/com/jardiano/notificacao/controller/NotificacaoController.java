package com.jardiano.notificacao.controller;

import com.jardiano.notificacao.exception.ResourceNotFoundException;
import com.jardiano.notificacao.model.Notificacao;
import com.jardiano.notificacao.repository.NotificacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.json.Json;
import javax.json.JsonObject;
import javax.validation.Valid;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


@RestController
@RequestMapping("/api/v1")
public class NotificacaoController {

  @Autowired
  private NotificacaoRepository notificacaoRepository;

  @GetMapping("/notificacao")
  public List<Notificacao> getAllNotiicacao() {
    return notificacaoRepository.findAll();
  }

  //ENVIA PUSH NOTIFICATION PARA OS DISPOSITIVOS MOBILE
  public void push(Notificacao notificacao) {
    try {
      String jsonResponse;

      URL url = new URL("https://onesignal.com/api/v1/notifications");
      HttpURLConnection con = (HttpURLConnection) url.openConnection();
      con.setUseCaches(false);
      con.setDoOutput(true);
      con.setDoInput(true);

      con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
      con.setRequestProperty("Authorization", "Basic NGU1ZjlhM2MtZDVjOC00ZTk1LWFkMTItMWE5Y2MzZWVmZjAx");
      con.setRequestMethod("POST");

      JsonObject empObject = Json.createObjectBuilder()
              .add("app_id", "43117305-1db5-4caa-9e17-9a8cf3c2900f")
              .add("included_segments",Json.createArrayBuilder().add("All").build())
              .add("data", Json.createObjectBuilder().add("foo", "bar").build())
              .add("headings", Json.createObjectBuilder().add("en",notificacao.getTitulo()).build())
              .add("contents", Json.createObjectBuilder().add("en",notificacao.getCorpo()).build()).build();

      byte[] sendBytes = String.valueOf(empObject).getBytes("UTF-8");
      con.setFixedLengthStreamingMode(sendBytes.length);


      OutputStream outputStream = con.getOutputStream();
      outputStream.write(sendBytes);

      int httpResponse = con.getResponseCode();
      System.out.println("httpResponse: " + httpResponse);

      if (httpResponse >= HttpURLConnection.HTTP_OK
              && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
        Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
        jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
        scanner.close();
      } else {
        Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
        jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
        scanner.close();
      }
      System.out.println("jsonResponse:\n" + jsonResponse);

    } catch (Throwable t) {
      t.printStackTrace();
    }
  }

  @GetMapping("/notificacao/{id}")
  public ResponseEntity<Notificacao> getUsersById(@PathVariable(value = "id") Long userId)
      throws ResourceNotFoundException {
    Notificacao notificacao = notificacaoRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found on :: " + userId));
    push(notificacao);
    return ResponseEntity.ok().body(notificacao);
  }


  @PostMapping("/notificacao")
  public Notificacao createUser(@Valid @RequestBody Notificacao notificacao) {
    return notificacaoRepository.save(notificacao);
  }


  @PutMapping("/notificacao/{id}")
  public ResponseEntity<Notificacao> updateUser(
      @PathVariable(value = "id") Long userId, @Valid @RequestBody Notificacao notificacaoDetails)
      throws ResourceNotFoundException {

    Notificacao notificacao =
        notificacaoRepository
            .findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found on :: " + userId));

    notificacao.setCorpo(notificacaoDetails.getCorpo());
    notificacao.setTitulo(notificacaoDetails.getTitulo());
    final Notificacao updatedNotificacao = notificacaoRepository.save(notificacao);
    return ResponseEntity.ok(updatedNotificacao);
  }

  @DeleteMapping("/notificacao/{id}")
  public Map<String, Boolean> deleteUser(@PathVariable(value = "id") Long userId) throws Exception {
    Notificacao notificacao =
        notificacaoRepository
            .findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found on :: " + userId));

    notificacaoRepository.delete(notificacao);
    Map<String, Boolean> response = new HashMap<>();
    response.put("deleted", Boolean.TRUE);
    return response;
  }
}
