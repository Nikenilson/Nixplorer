#include "WiFiEsp.h"
#include "SoftwareSerial.h"
#include <Stepper.h>
 
const int stepsPerRevolution = 4096;
Stepper rightAscension(stepsPerRevolution, 4,6,5,7);
Stepper declination(stepsPerRevolution, 10,12,11,13);
SoftwareSerial Serial1(8,9);
char ssid[] = "Gabriel";
char pass[] = "12345678";
int status = WL_IDLE_STATUS;

WiFiEspServer server(80);
char buf[10] = {0};

void setup() {
  rightAscension.setSpeed(1); //VELOCIDADE DO MOTOR
  declination.setSpeed(1);
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
        i++;
        if (strcmp("\r\n", buf) == 0){
          Serial.println("Fechando"); 
          break;
        }

        if (i == 9){
          char* vet = strtok(buf, ";");
          i = 0;
          while(vet != NULL ) {
            int coord = atoi(vet);
            Serial.println(coord);
            if(i == 0)
              moverRA(coord);
            else if (i == 1)
              moverDEC(coord);
            vet = strtok(NULL, ";");
            i++;
          }
          for(i = 0; i < strlen(buf); i++)
            buf[i] = 0;
          i = 0;
        }
      }
    }
    client.stop();
    Serial.println("Desconectado");
  }
}

void moverRA(int passos)
{
  rightAscension.step(passos);
}

void moverDEC(int passos)
{
  declination.step(passos);
}




