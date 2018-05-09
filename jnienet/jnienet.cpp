#include "enet/enet.h"
#include "jnienet.h"

#include <cstdint>

#ifdef _WIN32
#pragma comment(lib, "ws2_32.lib")
#pragma comment(lib, "winmm.lib")
#endif

typedef struct {
	unsigned int ev_type;
	unsigned int dataLength;
	uint64_t peerHandle;
}Event;

typedef struct {
	unsigned int host;
	unsigned int port;
}Address;

JNIEXPORT jint JNICALL Java_de_olfillasodikno_libenet_Enet_enet_1initialize(JNIEnv *, jclass) {
	return enet_initialize();
}

JNIEXPORT void JNICALL Java_de_olfillasodikno_libenet_Enet_enet_1deinitialize(JNIEnv *, jclass) {
	enet_deinitialize();
}

JNIEXPORT jlong JNICALL Java_de_olfillasodikno_libenet_Enet_enet_1host_1create(JNIEnv *env, jclass, jstring host, jshort port, jint outCon, jint ch, jint upstream, jint downstream) {
	ENetAddress address;

	const char *nativeString = (env)->GetStringUTFChars(host, 0);

	enet_address_set_host(&address, nativeString);

	address.port = port;
	(env)->ReleaseStringUTFChars(host,nativeString);

	ENetHost * client;
	if (port != 0) {
		client = enet_host_create(&address,
			outCon,
			ch,
			downstream,
			upstream);
	}
	else {
		client = enet_host_create(NULL,
			outCon,
			ch,
			downstream,
			upstream);
	}
	return (intptr_t)client;
}

JNIEXPORT void JNICALL Java_de_olfillasodikno_libenet_Enet_enet_1host_1destroy(JNIEnv *, jclass, jlong handle) {
	enet_host_destroy((ENetHost*)(intptr_t)handle);
}

JNIEXPORT jlong JNICALL Java_de_olfillasodikno_libenet_Enet_connect(JNIEnv *env, jclass, jlong hostHandle, jstring host, jint port, jint ch) {
	ENetAddress address;
	ENetPeer *peer;

	const char *nativeString = (env)->GetStringUTFChars(host, 0);

	enet_address_set_host(&address, nativeString);
	address.port = port;
	(env)->ReleaseStringUTFChars(host, nativeString);

	peer = enet_host_connect((ENetHost*)(intptr_t)hostHandle, &address, ch, 0);

	return (intptr_t)peer;
}

JNIEXPORT jlong JNICALL Java_de_olfillasodikno_libenet_Enet_enet_1host_1service(JNIEnv *, jclass, jlong handle, jint timeout) {
	ENetEvent *event = new ENetEvent;
	int ret = enet_host_service((ENetHost*)(intptr_t)handle, event, timeout);
	if (ret > 0) {
		return (intptr_t)(event);
	}
	else {
		return 0;
	}
}

JNIEXPORT jbyteArray JNICALL Java_de_olfillasodikno_libenet_Enet_getEvent(JNIEnv *env, jclass, jlong handle) {

	ENetEvent *enet_ev = (ENetEvent*)(intptr_t)handle;
	bool hasPacket = false;
	int dataLength = 0;

	if (enet_ev->type == ENET_EVENT_TYPE_RECEIVE) {
		dataLength = enet_ev->packet->dataLength;
		hasPacket = true;
	}

	jbyteArray array = (env)->NewByteArray(sizeof(Event)+ dataLength);

	Event ev;

	ev.ev_type = enet_ev->type;
	ev.dataLength = dataLength;
	ev.peerHandle = (intptr_t)enet_ev->peer;
	(env)->SetByteArrayRegion(array, 0, sizeof(Event), (jbyte *)&ev);

	if (hasPacket) {
		(env)->SetByteArrayRegion(array, sizeof(Event), dataLength, (jbyte *)enet_ev->packet->data);
		enet_packet_destroy(enet_ev->packet);
	}

	return array;
}

JNIEXPORT jlong JNICALL Java_de_olfillasodikno_libenet_Enet_enet_1packet_1create(JNIEnv *env, jclass, jbyteArray data, jint length, jint flags) {

	jbyte * ptr = (env)->GetByteArrayElements(data, 0);
	ENetPacket *ret = enet_packet_create(ptr, length, flags);

	return (intptr_t)ret;
}

JNIEXPORT void JNICALL Java_de_olfillasodikno_libenet_Enet_enet_1packet_1destroy(JNIEnv *, jclass, jlong handle) {
	enet_packet_destroy((ENetPacket*)(intptr_t)handle);
}

JNIEXPORT void JNICALL Java_de_olfillasodikno_libenet_Enet_enet_1peer_1send(JNIEnv *, jclass, jlong handleH, jchar ch, jlong handleP) {
	enet_peer_send((ENetPeer*)(intptr_t)handleH, ch, (ENetPacket*)(intptr_t)handleP);
}

JNIEXPORT void JNICALL Java_de_olfillasodikno_libenet_Enet_enet_1host_1flush(JNIEnv *, jclass, jlong handle) {
	enet_host_flush((ENetHost*)(intptr_t)handle);
}

JNIEXPORT void JNICALL Java_de_olfillasodikno_libenet_Enet_enet_1host_1broadcast
(JNIEnv *, jclass, jlong handleH, jchar ch, jlong handleP) {
	enet_host_broadcast((ENetHost*)(intptr_t)handleH, ch, (ENetPacket*)(intptr_t)handleP);
}

JNIEXPORT jbyteArray JNICALL Java_de_olfillasodikno_libenet_Enet_getAddress(JNIEnv *env, jclass, jlong handle) {

	ENetPeer *peer = (ENetPeer*)(intptr_t)handle;
	Address addr;
	jbyteArray array = (env)->NewByteArray(sizeof(Address));

	addr.host = peer->address.host;
	addr.port = peer->address.port;

	(env)->SetByteArrayRegion(array, 0, sizeof(Address), (jbyte *)&addr);

	return array;
}

JNIEXPORT void JNICALL Java_de_olfillasodikno_libenet_Enet_enet_1peer_1destroy(JNIEnv *, jclass, jlong handle) {
	ENetPeer *peer = (ENetPeer*)(intptr_t)handle;
	enet_peer_disconnect_now(peer, 0);
}