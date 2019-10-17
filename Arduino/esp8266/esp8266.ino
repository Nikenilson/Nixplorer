#include <SoftwareSerial.h>
SoftwareSerial esp8266 (8, 9); //RX, TX
void setup() {
  Serial.begin(115200);
  while(!Serial){;
  }
  Serial.println("GOOD MORNING");

  esp8266.begin(9600);
  esp8266.println("AT");
}

void loop() {
  if(esp8266.available()){
    Serial.write(esp8266.read());
  }
  if(Serial.available()){
    esp8266.write(Serial.read());
  }
}
