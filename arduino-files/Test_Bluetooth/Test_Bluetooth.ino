const int ledRed = 9;
const int ledGreen = 10;
const int ledBlue = 11;

const int ledRed2 = 3;
const int ledGreen2 = 5;
const int ledBlue2 = 6;

#define REDPIN 9
#define GREENPIN 10
#define BLUEPIN 11

#define FADESPEED 5  

void setup()
{
  Serial.begin(9600);
  pinMode(ledRed, OUTPUT); 
  pinMode(ledGreen, OUTPUT); 
  pinMode(ledBlue, OUTPUT); 
  pinMode(ledRed2, OUTPUT); 
  pinMode(ledGreen2, OUTPUT); 
  pinMode(ledBlue2, OUTPUT); 
  //Serial.print("Arduino control RGB LEDs Connected OK ( Sent From Arduinno Board )");
  //Serial.print('\n');
}

void loop()
{  
  while (Serial.available() > 0)
  {   
    int red = Serial.parseInt(); 
    int green = Serial.parseInt();
    int blue = Serial.parseInt();
    int red2 = Serial.parseInt();
    int green2 = Serial.parseInt();
    int blue2 = Serial.parseInt();
    if (Serial.read() == '\n') 
    {           
      // constrain the values to 0 - 255 and invert
      // if you're using a common-cathode LED, just use "constrain(color, 0, 255);"
      
      red = 255 - constrain(red, 0, 255);
      green = 255 - constrain(green, 0, 255);
      blue = 255 - constrain(blue, 0, 255);

      red2 = 255 - constrain(red2, 0, 255);
      green2 = 255 - constrain(green2, 0, 255);
      blue2 = 255 - constrain(blue2, 0, 255);
      
      //red = constrain(red, 0, 255);
      //green = constrain(green, 0, 255);
      //blue = constrain(blue, 0, 255);
      
      //red2 = constrain(red2, 0, 255);
      //green2 = constrain(green2, 0, 255);
      //blue2 = constrain(blue2, 0, 255);

      // fade the red, green, and blue legs of the LED: 
      analogWrite(ledRed, red);
      analogWrite(ledGreen, green);
      analogWrite(ledBlue, blue);
      
      analogWrite(ledRed2, red);
      analogWrite(ledGreen2, green);
      analogWrite(ledBlue2, blue);

      // print the three numbers in one string as hexadecimal:
      // Serial.print("Data Response : ");
      //Serial.print(red, HEX);
      //Serial.print(green, HEX);
      //Serial.println(blue, HEX);
    }
  }
  
}
