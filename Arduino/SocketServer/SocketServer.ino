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
char buf[10] = {0};

void setup() {
  myStepper.setSpeed(1); //VELOCIDADE DO MOTOR
  
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
    int i = 0;
    while (client.connected()){
      if (client.available()){
        char c = client.read();
        buf[i] = c;
        if (strcmp("\r\n\r\n", buf) == 0){
          Serial.println("Fechando"); 
          break;
        }

        if (i == 9){
          char delimiter = '-';
          char* vet = strtok(buf, &delimiter);
          Serial.println(vet[0]);
          //efeito1();
          //reset
          i = 0;
          Serial.println(buf);
          Serial.println(" Efeito 1");
        }
        if (strcmp("OFF", buf) == 0){
          digitalWrite(13, LOW);
          //reset
          i = 0;
          Serial.println(" Ligado");
        }
        
        
      }
    } // while
    client.stop();
    Serial.println("Desconectado");
  }
}

void efeito1(int graus)
{
  int passos = graus*2048/360;
  myStepper.step(passos);
}




