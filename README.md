# shiftlab_back_testtask

***БД*** \
Используемая база данных: H2\
Для установки можно воспользоваться гайдом https://coderlessons.com/tutorials/bazy-dannykh/izuchite-bazu-dannykh-h2/baza-dannykh-h2-ustanovka \
Необходимо не забыть добавить jar файл в dependences \
База данных после запуска программы будет содержать таблицы (но без данных)

***Запуск программы*** \
Для запуска программы нужно запустить main из класса MainApp.
По адресу http://localhost:8007/test находится "Hello"

***Примеры для просмотра по series_number*** \
http://localhost:8007/test?id=10

***Примеры для просмотра по типу***\
http://localhost:8007/test?type=laptop

***Пример создания нового продукта***
```
POST /test? HTTP/1.1
Host: localhost:8007
Cache-Control: no-cache
Postman-Token: 8ed5663c-1d05-5e3c-fc08-9b6567fa27a6
Content-Type: multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW

------WebKitFormBoundary7MA4YWxkTrZu0gW
Content-Disposition: form-data; name="action"

insert
------WebKitFormBoundary7MA4YWxkTrZu0gW
Content-Disposition: form-data; name="table"

laptop
------WebKitFormBoundary7MA4YWxkTrZu0gW
Content-Disposition: form-data; name="series_number"

11
------WebKitFormBoundary7MA4YWxkTrZu0gW
Content-Disposition: form-data; name="price"

10
------WebKitFormBoundary7MA4YWxkTrZu0gW--
```
***Пример изменения проодукта***
```
POST /test? HTTP/1.1
Host: localhost:8007
Cache-Control: no-cache
Postman-Token: e93756ac-7f58-f373-3a88-af072b33e979
Content-Type: multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW

------WebKitFormBoundary7MA4YWxkTrZu0gW
Content-Disposition: form-data; name="action"

update
------WebKitFormBoundary7MA4YWxkTrZu0gW
Content-Disposition: form-data; name="table"

laptop
------WebKitFormBoundary7MA4YWxkTrZu0gW
Content-Disposition: form-data; name="series_number"

11
------WebKitFormBoundary7MA4YWxkTrZu0gW
Content-Disposition: form-data; name="price"

11
------WebKitFormBoundary7MA4YWxkTrZu0gW--
```
