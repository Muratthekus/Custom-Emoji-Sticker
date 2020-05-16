# Custom Emoji Sticker
Custom Emoji Sticker app allow to user create their own emoji, create packages and upload package to the Gboard or upgrade the package that is already store in Gboard.
User can use custom emojis as sticker on Whatsapp.
Application built on MVVM android architecture.

## Screens
![emoji-mashup](https://user-images.githubusercontent.com/45212967/82121680-8b3b5a00-9797-11ea-8d58-e62fbccbdd1c.png)



### Requirements
Gboard must be installed otherwise user can not use the application.

### About
Application fetch images from assets folder and according to user decision combine them as one emoji. Package and emoji names store in local database, emojis image store in application file according to package and firebase storage. If user want to see him/her emojis, images fetch from files. If user want to upload/upgrade package images fetch from firebase and send to the Gboard.

### Built With
- Kotlin
- LiveData
- Android Room Library
- ViewModel
- Job Intent Service

###Architecture
> MVVM Architecture =Model + View +ViewModel


###### Licence
[MIT](https://choosealicense.com/licenses/mit/)



