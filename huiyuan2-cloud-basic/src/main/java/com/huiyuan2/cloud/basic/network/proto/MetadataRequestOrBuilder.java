// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: registry.proto

package com.huiyuan2.cloud.basic.network.proto;

public interface MetadataRequestOrBuilder extends
    // @@protoc_insertion_point(interface_extends:com.huiyuan2.cloud.basic.registry.proto.MetadataRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>string key = 1;</code>
   * @return The key.
   */
  java.lang.String getKey();
  /**
   * <code>string key = 1;</code>
   * @return The bytes for key.
   */
  com.google.protobuf.ByteString
      getKeyBytes();

  /**
   * <code>string value = 2;</code>
   * @return The value.
   */
  java.lang.String getValue();
  /**
   * <code>string value = 2;</code>
   * @return The bytes for value.
   */
  com.google.protobuf.ByteString
      getValueBytes();

  /**
   * <code>string serviceType = 3;</code>
   * @return The serviceType.
   */
  java.lang.String getServiceType();
  /**
   * <code>string serviceType = 3;</code>
   * @return The bytes for serviceType.
   */
  com.google.protobuf.ByteString
      getServiceTypeBytes();

  /**
   * <code>int32 event = 4;</code>
   * @return The event.
   */
  int getEvent();
}
