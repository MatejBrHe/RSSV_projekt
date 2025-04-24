#include <SoftwareSerial.h>

int rPin = 3;
int gPin = 5;
int bPin = 6;
int tx = 1;
int rx = 0;

int r = 0;
int g = 0;
int b = 0;
String data = "";


SoftwareSerial BTSerial (rx, tx);

void setup() {
  Serial.begin(9600);
  BTSerial.begin(9600);
  pinMode(rPin, OUTPUT);
  pinMode(gPin, OUTPUT);
  pinMode(bPin, OUTPUT);
}

void loop() {
  if(BTSerial.available()){
    Serial.write(BTSerial.read());
    data = BTSerial.readString();
    r = data.substring(0, 3).toInt(); 
    g = data.substring(3, 6).toInt();
    b = data.substring(6, 9).toInt();
    Serial.write(r);
    Serial.write(g);
    Serial.write(b);
  }

  analogWrite(rPin, r);
  analogWrite(gPin, g);
  analogWrite(bPin, b);   
  delay(100);                      
}
