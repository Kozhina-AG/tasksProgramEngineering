# Produser-Consumer
В рамках данной работы была реализована заданная ситуация для моделирования "Работа порта", используя потоки для работы объектов.

Процесс-производитель (producer) генерирует в некотором буфере информацию, которая используется процессом-потребителем (consumer). 
* Producer (Поставщик) - это некоторый поток, который генерирует “задания” и складывает их в очередь Queue.
* Consumer (Потребитель) - это поток, который берет задания из очереди, выполняет и отправляет результаты туда, куда нужно.
* Очередь Queue (очередь продуктов) - это ограниченный буфер заданий с заранее заданной вместимостью.

## Рабочие окна программы

Скриншот _"стартового"_ окна

![Рабочее окно программы](https://github.com/Kozhina-AG/tasksProgramEngineering/blob/main/task_17/Старт.png)

Скриншот _"рабочего"_ окна

![Рабочее окно программы](https://github.com/Kozhina-AG/tasksProgramEngineering/blob/main/task_17/Работа.png)

Скриншот окна при _"остановке"_ работы

![Рабочее окно программы](https://github.com/Kozhina-AG/tasksProgramEngineering/blob/main/task_17/Стоп.png)