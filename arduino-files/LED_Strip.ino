// this script is for controlling a chain of RGB led lights

// defining the 74HC595 Shift Register input pins
#define DS 4
#define ST_CP 7
#define SH_CP 8

// defining the RGB led input pins (analog pins)
#define REDPIN 9
#define GREENPIN 10
#define BLUEPIN 11

#define FADESPEED 5

byte leds = 0;

void setup()
{
  Serial.begin(9600);
  pinMode(REDPIN, OUTPUT); 
  digitalWrite(REDPIN,HIGH);
  pinMode(GREENPIN, OUTPUT);
  digitalWrite(GREENPIN,HIGH); 
  pinMode(BLUEPIN, OUTPUT); 
  digitalWrite(BLUEPIN,HIGH);
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
       
      analogWrite(REDPIN, red);
      analogWrite(GREENPIN, green);
      analogWrite(BLUEPIN, blue);
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
