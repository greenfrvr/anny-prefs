# AnnyPrefs

Android annotation based `SharedPreferences` wrapper with fluent interface.

## How to use

First of all we need to declare what kind of data we want to persist. For example:
```java
@AnnyPref(name = "user")
public interface User {
    @StringPref(key = "user_name", value = "Arty")
    void username(String username);
    
    @StringPref(keyRes = R.string.user_age)
    void age(int username);
    
    @StringPref(value = true)
    void firstLaunch(boolean isFirst);
    
    @DatePref
    void birthday(Date birthday);
    
    @ObjectPref(key = "user_address", type = Address.class)
    void address(Address address)
}
```
`@AnnyPref` annotation contains `name` field which defines whether to store these preferences in separate file or in default shared preferences file (default behavior). For current example prefs will be stored in `(package)_anny_user.xml` while default prefs stored in `(package)_preferences.xml` This annotation can be used only with interfaces. 

`@StringPref` annotation defines stored property, i.e. it defines type, key and default value of stored property. There are two params `key` and `value`, which are both optional. 

`key` parameter defines the key of property which will be stored in shared preferences, if not defined method name will be used instead. 

`keyRes` parameter defines string resource which will be used for this property.

`value` parameter defines default value which will be returned in case if the property have never been persisted before, there are predefined default values for each type of stored properties (String, Integer, Float etc.). 

After declaring properties you have to rebuild the project. And you will get the following:
```java
  UserPrefs prefs = AnnyPrefs.user(Context);
  
  SaveUser saveUser = prefs.save();
  RestoreUser restoreUser = prefs.restore();
  RemoveUser removeUser = prefs.remove();
  
  saveUser.username("greenfrvr").async();
  restoreUser.username();
  boolean isRemoved = removeUser.username().sync();
```
There are several generated classes which are based on declared interface.
First of all main class which let you doing things with shared prefs is obtained by adding `Prefs` to you declared interface name. In our case it is `UserPrefs`. The only way to get an instance of it is to call `AnnyPrefs` method corresponding to that prefs. That method name is an interface name in lowercase. So when we call `AnnyPrefs.user(context)` we will get `UserPrefs` instance which can return 3 base interfaces, that are `SaveUser`, `RestoreUser` and `RemoveUser`. 
These interfaces are returned by calling corresponding methods `save()`, `restore()` and `remove()`.
Each of these interfaces let you work with stored properties with named methods (the methods names are exactly the same that declared in your interface with `@AnnyPref` annotation).

##Supported property types
All types which are supported by `SharedPreferences` are obviously cann be stored (`String`, `Int`, `Float`, `Long`, `Boolean`, `StringSet`). Also you can store `Date` properties and in fact any POJO which can be transformed to JSON by `com.google.gson` library. 

Below is a list of all available annotations:
- `@BooleanPref` predefined value is `false`
- `@IntPref` predefined value is 0 
- `@FloatPref` predefined value is 0.0f
- `@LongPref` predefined value is 0L
- `@StringPref` predefined value is ""
- `@StringSetPref` predefined value is null
- `@DatePref` predefined value is `Date` object with 0  milliseconds
- `@ObjectPref` predefined value is null. Instead of `value` property it has `type` property which is class representing  this property.

There are no requirements about how annotated methods should look like, so you can define it in a way which let you use it later. 

##Saving preferences
Just like in `SharedPreferences` there are 2 ways to modify preferences by commiting (by calling `sync()`) and applying ( by calling `async()`) changes. For example:
```java
  SaveUser saveUser = prefs.save();
  saveUser.username("greenfrvr");
  saveUser.age(23);
  saveUser.firstLaunch(false);
  
  boolean isPersisted = saveUser.sync(); //or you can call async() instead
```

Fluent inteface lets you saving multiple properties just with one calls chain. 
```java
  AnnyPrefs.user(Context).save().username("greenfrvr").age(23).firstLaunch(false).async();
``` 

##Retreiving preferences
Retreiving properties is very simple. 
```java
  RestoreUser restoreUser = prefs.restore();

  String name = restoreUser.username();
  int age = restoreUser.age();
  boolean isFirstLaunch = restoreUser.firstLaunch();
```
or if you need only one property 
```java
  AnnyPrefs.user(Context).restore().firstLaunch();
```
##Removing preferences
Removing propery values require to call `sync()` or `async()` to apply changes just like in saving preferences.
```java
  boolean isRemoved = AnnyPrefs.user(Context).remove().username().age().async();
```
To clear all preferences just call `clear()` method.
```java
  AnnyPrefs.user(Context).clear();
```

##Preferences listeners
To track all preferences changes you can add `SharedPreferences.OnSharedPreferenceChangeListener`.
```java
  AnnyPrefs.user(Context).registerListener(SharedPreferences.OnSharedPreferenceChangeListener)
```
To stop tracking unregister that listener by calling `unregisterListener(SharedPreferences.OnSharedPreferenceChangeListener)`.

## License

     Copyright 2015 greenfrvr

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
