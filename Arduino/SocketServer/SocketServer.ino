#include "WiFiEsp.h"
#include "SoftwareSerial.h"
#include <Stepper.h>
 
const int stepsPerRevolution = 4096;
Stepper myStepper(stepsPerRevolution, 4,6,5,7);
SoftwareSerial Serial1(8,9);
char ssid[] = "Gabriel";
char pass[] = "12345678";
int status = WL_IDLE_STATUS;

WiFiEspServer server(80);
RingBuffer buf(8);

void setup() {
  myStepper.setSpeed(300); //VELOCIDADE DO MOTOR
  
  Serial.begin(115200);  // porta de debug
  Serial1.begin(9600);
  WiFi.init(&Serial1);
  if (WiFi.status() == WL_NO_SHIELD) {
    Serial.println("Sem modulo");
    while(true);  //trava a execução 
  }
  while (status != WL_CONNECTED){
    Serial.println("tentando conectar...");
    status = WiFi.begin(ssid,pass);
  }
  Serial.println("Conectado...");
  IPAddress ip = WiFi.localIP();
  Serial.println(ip);
  server.begin();
}

void loop() {
  WiFiEspClient client = server.available();
  if (client){
    Serial.println("Novo cliente");
    buf.init();

    while (client.connected()){
      if (client.available()){
        char c = client.read();
        buf.push(c);
        Serial.write(c);
        if (buf.endsWith("\r\n\r\n")){
          Serial.println("Fechando"); 
          break;
        }

        if (buf.endsWith("1")){
          efeito1();
          buf.reset();
          Serial.println(" Efeito 1");
        }
        if (buf.endsWith("OFF")){
          digitalWrite(13, LOW);
          buf.reset();
          Serial.println(" Ligado");
        }
        
        
      }
    } // while
    client.stop();
    Serial.println("Desconectado");
  }
}

void efeito1()
{    
  myStepper.step(64);
}




