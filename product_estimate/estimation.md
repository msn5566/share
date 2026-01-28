Weightage – Engineering Excellence
Parameter	Weight (%)
Code Quality	10%
Test Coverage	50%
CI/CD Maturity	20%
Security Posture	10%
IDP Adoption	10%
Total	100% ~ 1

Weightage – AI Maturity
Parameter	Weight (%)
AI Maturity	100% ~ 1

Total Technology Factor = Engineering Excellence + AI Maturity
	                                  = 1 + 1
	                                  = 2

Tech factor is used to determine the effort required to deliver the story point in person days
higher the maturity of Engineering, lower the tech factor
lower the maturity of Engineering, higher the tech factor

AI Maturity values
Yet to start : 0
Initial Adoption : 0.25
Tool as Assistant : 0.5
Supervised Agent: 0.75
Autonomous Agent: 1
Sample values 
Parameter	Values
Code Quality	50% ~ 0.5
Test Coverage	45% ~ 0.45
CI/CD Maturity	50% ~ 0.5
Security Posture	60% ~ 0.6
IDP Adoption	40% ~ 0.4
AI Maturity	0

Tech factor = (1-Code Quality) * Code Quality weightage
+ (1-Test Coverage) * Test Coverage weightage
+ (1-CI/CD maturity) * CI/CD maturity weightage
+ (1-Security posture)* Security Posture weightage
+ (1-IDP Adoption) * IDP Adoption weightage
+ (1-AI maturity) * AI maturity weightage
0.05 + 0.225 + 0.10+ 0.06 + 0.04+ 1
=1.475

Tech factor = (1-0.5)*0.1 + (1-0.45)*0.5 + (1-0.5)*0.2 + (1-0.6)*0.1 + (1-0.4)*0.1 + (1-0)*1
= 0.5*0.1 + 0.55*0.1 + 0.5*0.2 + 0.4*0.1 + 0.6*0.1 + 1
= 0.05+0.055+0.10+0.04+0.06 + 1
= 1.305

