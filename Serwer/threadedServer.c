#include <sys/types.h>
#include <sys/wait.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <netdb.h>
#include <signal.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <pthread.h>
#include <string.h>
#define SERVER_PORT 1234
#define QUEUE_SIZE 5

int count=0;
pthread_mutex_t count_mutex = PTHREAD_MUTEX_INITIALIZER;

//struktura zawierająca dane, które zostaną przekazane do wątku
struct thread_data_t
{
    int connection_socket_descriptor;
	int pipeIndex;
};

//funkcja opisującą zachowanie wątku - musi przyjmować argument typu (void *) i zwracać (void *)
void *WhiteThreadBehavior(void *t_data)
{
    pthread_detach(pthread_self());
    struct thread_data_t *th_data = (struct thread_data_t*)t_data;
    int n = write((*th_data).connection_socket_descriptor,"BIALY\n",7);
        if (n < 0) {
			fprintf(stderr,"Błąd przy wysylaniu koloru");
			exit(-1);
		}

    pthread_exit(NULL);
}
void *BlackThreadBehavior(void *t_data)
{
    pthread_detach(pthread_self());
    struct thread_data_t *th_data = (struct thread_data_t*)t_data;
    //dostęp do pól struktury: (*th_data).pole
	int n = write((*th_data).connection_socket_descriptor,"CZARNY\n",7);
        if (n < 0) {
			fprintf(stderr,"Błąd przy wysylaniu koloru");
			exit(-1);
		}

    pthread_exit(NULL);
}

//funkcja obsługująca połączenie z nowym klientem
void handleConnection(int connection_socket_descriptor, struct sockaddr_in *client_adress) {
    //blokowanie mutexa, inkrementowanie count
	pthread_mutex_lock(&count_mutex);
	count++;
	int create_result=0;
    //dane, które zostaną przekazane do wątku
    struct thread_data_t t_data;
	t_data.connection_socket_descriptor=connection_socket_descriptor;
	//przekazujemy indeks w tablicy deskryptorów, wskazujący na deskryptor za pomoca ktorego beda komunikowac sie watki
	t_data.pipeIndex=(count-1)/2;
    //uchwyt na wątek
    pthread_t thread1;
	//przypadek gdy watek musi czekac na zgloszenie kolejnego gracza, ponieważ brakuje pary- otrzymuje czarny kolor pionkow 
	if(count%2==1){
		create_result = pthread_create(&thread1, NULL, BlackThreadBehavior, &t_data);
	}
	//przypadek gdy mamy dwóch niesparowanych graczy- drugi gracz otrzymuje kolor bialy
	else{
		create_result = pthread_create(&thread1, NULL, WhiteThreadBehavior, &t_data);
	}
    if (create_result){
       fprintf(stderr,"Błąd przy próbie utworzenia wątku, kod błędu: %d\n", create_result);
       exit(-1);
    }
	pthread_mutex_unlock(&count_mutex);
	
	
	

    //TODO (przy zadaniu 1) odbieranie -> wyświetlanie albo klawiatura -> wysyłanie
}

int main(int argc, char* argv[])
{
   int server_socket_descriptor;
   int connection_socket_descriptor;
   int bind_result;
   int listen_result;
   char reuse_addr_val = 1;
   struct sockaddr_in server_address;
     server_socket_descriptor = socket(AF_INET, SOCK_STREAM, 0);
   if (server_socket_descriptor < 0)
   {
       fprintf(stderr, "%s: Błąd przy próbieeeee utworzenia gniazda..\n", argv[0]);
       exit(1);
   }

   //inicjalizacja gniazda serwera
   
   memset(&server_address, 0, sizeof(struct sockaddr));
   server_address.sin_family = AF_INET;
   server_address.sin_addr.s_addr = htonl(INADDR_ANY);
   server_address.sin_port = htons(SERVER_PORT);

 
   setsockopt(server_socket_descriptor, SOL_SOCKET, SO_REUSEADDR, (char*)&reuse_addr_val, sizeof(reuse_addr_val));

   bind_result = bind(server_socket_descriptor, (struct sockaddr*)&server_address, sizeof(struct sockaddr));
   if (bind_result < 0)
   {
       fprintf(stderr, "%s: Błąd przy próbie dowiązania adresu IP i numeru portu do gniazda.\n", argv[0]);
       exit(1);
   }

   listen_result = listen(server_socket_descriptor, QUEUE_SIZE);
   if (listen_result < 0) {
       fprintf(stderr, "%s: Błąd przy próbie ustawienia wielkości kolejki.\n", argv[0]);
       exit(1);
   }

   while(1)
   {
       struct sockaddr_in client_address;
	   int clilen = sizeof(client_address);
       connection_socket_descriptor = accept(server_socket_descriptor, (struct sockaddr *)&client_address, &clilen);
       if (connection_socket_descriptor < 0)
       {
           fprintf(stderr, "%s: Błąd przy próbie utworzenia gniazda dla połączenia.\n", argv[0]);
           exit(1);
       }

       handleConnection(connection_socket_descriptor, &client_address);
   }

   close(server_socket_descriptor);
   return(0);
}
