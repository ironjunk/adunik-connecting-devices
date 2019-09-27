const int DS = 4;
const int ST_CP = 7;
const int SH_CP = 8;

const int ledRed = 9;
const int ledGreen = 10;
const int ledBlue = 11;

#define REDPIN 9
#define GREENPIN 10
#define BLUEPIN 11

#define FADESPEED 5

byte leds = 0;

void setup()
{
  Serial.begin(9600);
  pinMode(ledRed, OUTPUT); 
  digitalWrite(ledRed,HIGH);
  pinMode(ledGreen, OUTPUT);
  digitalWrite(ledGreen,HIGH); 
  pinMode(ledBlue, OUTPUT); 
  digitalWrite(ledBlue,HIGH);
  pinMode(DS, OUTPUT);
  pinMode(ST_CP, OUTPUT);
  pinMode(SH_CP, OUTPUT);
}

void loop()
{
  leds = 0;
  updateShiftRegister();
  while (Serial.available() > 0)
  {
    int red = Serial.parseInt(); 
    int green = Serial.parseInt();
    int blue = Serial.parseInt();
    if (Serial.read() == '\n')
    {           
      red = 255 - constrain(red, 0, 255);
      green = 255 - constrain(green, 0, 255);
      blue = 255 - constrain(blue, 0, 255);
       
      analogWrite(ledRed, red);
      analogWrite(ledGreen, green);
      analogWrite(ledBlue, blue);
    }
  }
}

void updateShiftRegister()
{
   digitalWrite(ST_CP, LOW);
   shiftOut(DS, SH_CP, LSBFIRST, leds);
   shiftOut(DS, SH_CP, LSBFIRST, leds);
   shiftOut(DS, SH_CP, LSBFIRST, leds);
   digitalWrite(ST_CP, HIGH);
}
