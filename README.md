# floatingYoutubePopUpWindow



step1. Add this to root build.gradle

       allprojects {
         repositories {
		...
		maven { url 'https://jitpack.io' }
	   }
	}
	

step2. Add this to app build.gradle

    dependencies {
	        implementation 'com.github.FarazAhmedFeb1998:FloatingYoutubePopUP:1.3'
	  }
	  
	  
 step3. To trigger the Floating youtube popup add this code in mainActivity or any activity or fragment where you want to show:
      
	  clickable.setOnClickListener {
            FloatingYoutubePopUp().floatingPopUp(this,view,VideoId)
        }
	
view = can be Image,text,button,cardView etc


VideoID: this is the 11 digit youtube video id
      example:"B1l8KIzcoRM"
      
 NOTE:
    minSdkVersion should be grater than 17
    
    
 Demo:

https://user-images.githubusercontent.com/86732801/124475746-b16af180-ddbf-11eb-9c77-6ebad3a3bf5a.mp4



