#include <stdio.h> 
#include <assert.h>
#include <math.h>
#include <stdlib.h>

	// inputs
	int inputs[] = {1,2,5,4,3};

	const int error_0 = 1;
	int cf = 1;
	int a134 = 2;


	void errorCheck() {
	    if((a134==1)){
	    cf = (0);
	    error_99: assert(!error_0);
	    }
	}
 
void calculate_outputm37(int input) {
	if(a134==2){
		calculate_outputm40(input);
	}
	if(a134==3){
		a134=1;
	}
} void calculate_outputm40(int input) {
    a134=3;
} void calculate_outputm39(int input) {
    if(((a134==10) && (input==1))){
    	calculate_outputm40(input);
    } 
}
 void calculate_output(int input) {
        cf = 1;
	calculate_outputm37(input);
    errorCheck();

//    if((cf==1)) {
  //  	fprintf(stderr, "Invalid input: %d\n", input);
    //	 }
}

int main()
{
    //srand((unsigned)time(NULL));
    // main i/o-loop
    while(1)
    {
        // read input
        int input;
        scanf("%d", &input);        
        // operate eca engine
        calculate_output(input);
    }
}