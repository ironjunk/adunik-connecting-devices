int led = 13;
int input;

void setup()
{
  pinMode(led, OUTPUT);
  Serial.begin(9600);
  Serial.println("Enter value: ");
  Serial.println();
}

void loop()
{
  if(Serial.available() > 0)
  {
    input = Serial.read();
    if(input == '0')
    {
      digitalWrite(led, LOW);
      Serial.println("Left");
    }
    if(input == '1')
    {
      digitalWrite(led, HIGH);
      Serial.println("Right");
    }
  }
}
