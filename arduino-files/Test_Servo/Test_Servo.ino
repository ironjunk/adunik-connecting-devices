#include <Servo.h>

Servo myServo;

void setup()
{
  myServo.attach(9);
  myServo.write(90);
}

void loop()
{
  myServo.write(30);
  myServo.write(120);
}

