// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: tracking.proto

package com.imprexion.service.tracking.bean;

/**
 * Protobuf type {@code tracking.user_info}
 */
public  final class user_info extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:tracking.user_info)
    user_infoOrBuilder {
private static final long serialVersionUID = 0L;
  // Use user_info.newBuilder() to construct.
  private user_info(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private user_info() {
    validHand_ = 0;
    dynamicGestureId_ = 0;
    leftArmPostureId_ = 0;
    rightArmPostureId_ = 0;
    handGestureId_ = 0;
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private user_info(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    if (extensionRegistry == null) {
      throw new java.lang.NullPointerException();
    }
    int mutable_bitField0_ = 0;
    com.google.protobuf.UnknownFieldSet.Builder unknownFields =
        com.google.protobuf.UnknownFieldSet.newBuilder();
    try {
      boolean done = false;
      while (!done) {
        int tag = input.readTag();
        switch (tag) {
          case 0:
            done = true;
            break;
          default: {
            if (!parseUnknownFieldProto3(
                input, unknownFields, extensionRegistry, tag)) {
              done = true;
            }
            break;
          }
          case 8: {

            validHand_ = input.readInt32();
            break;
          }
          case 16: {
            int rawValue = input.readEnum();

            dynamicGestureId_ = rawValue;
            break;
          }
          case 24: {
            int rawValue = input.readEnum();

            leftArmPostureId_ = rawValue;
            break;
          }
          case 32: {
            int rawValue = input.readEnum();

            rightArmPostureId_ = rawValue;
            break;
          }
          case 40: {
            int rawValue = input.readEnum();

            handGestureId_ = rawValue;
            break;
          }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw e.setUnfinishedMessage(this);
    } catch (java.io.IOException e) {
      throw new com.google.protobuf.InvalidProtocolBufferException(
          e).setUnfinishedMessage(this);
    } finally {
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.imprexion.service.tracking.bean.TrackingMessage.internal_static_tracking_user_info_descriptor;
  }

  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.imprexion.service.tracking.bean.TrackingMessage.internal_static_tracking_user_info_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.imprexion.service.tracking.bean.user_info.class, com.imprexion.service.tracking.bean.user_info.Builder.class);
  }

  public static final int VALID_HAND_FIELD_NUMBER = 1;
  private int validHand_;
  /**
   * <code>int32 valid_hand = 1;</code>
   */
  public int getValidHand() {
    return validHand_;
  }

  public static final int DYNAMIC_GESTURE_ID_FIELD_NUMBER = 2;
  private int dynamicGestureId_;
  /**
   * <code>.tracking.dynamic_gesture dynamic_gesture_id = 2;</code>
   */
  public int getDynamicGestureIdValue() {
    return dynamicGestureId_;
  }
  /**
   * <code>.tracking.dynamic_gesture dynamic_gesture_id = 2;</code>
   */
  public com.imprexion.service.tracking.bean.dynamic_gesture getDynamicGestureId() {
    com.imprexion.service.tracking.bean.dynamic_gesture result = com.imprexion.service.tracking.bean.dynamic_gesture.valueOf(dynamicGestureId_);
    return result == null ? com.imprexion.service.tracking.bean.dynamic_gesture.UNRECOGNIZED : result;
  }

  public static final int LEFT_ARM_POSTURE_ID_FIELD_NUMBER = 3;
  private int leftArmPostureId_;
  /**
   * <code>.tracking.arm_posture left_arm_posture_id = 3;</code>
   */
  public int getLeftArmPostureIdValue() {
    return leftArmPostureId_;
  }
  /**
   * <code>.tracking.arm_posture left_arm_posture_id = 3;</code>
   */
  public com.imprexion.service.tracking.bean.arm_posture getLeftArmPostureId() {
    com.imprexion.service.tracking.bean.arm_posture result = com.imprexion.service.tracking.bean.arm_posture.valueOf(leftArmPostureId_);
    return result == null ? com.imprexion.service.tracking.bean.arm_posture.UNRECOGNIZED : result;
  }

  public static final int RIGHT_ARM_POSTURE_ID_FIELD_NUMBER = 4;
  private int rightArmPostureId_;
  /**
   * <code>.tracking.arm_posture right_arm_posture_id = 4;</code>
   */
  public int getRightArmPostureIdValue() {
    return rightArmPostureId_;
  }
  /**
   * <code>.tracking.arm_posture right_arm_posture_id = 4;</code>
   */
  public com.imprexion.service.tracking.bean.arm_posture getRightArmPostureId() {
    com.imprexion.service.tracking.bean.arm_posture result = com.imprexion.service.tracking.bean.arm_posture.valueOf(rightArmPostureId_);
    return result == null ? com.imprexion.service.tracking.bean.arm_posture.UNRECOGNIZED : result;
  }

  public static final int HAND_GESTURE_ID_FIELD_NUMBER = 5;
  private int handGestureId_;
  /**
   * <code>.tracking.hand_gesture hand_gesture_id = 5;</code>
   */
  public int getHandGestureIdValue() {
    return handGestureId_;
  }
  /**
   * <code>.tracking.hand_gesture hand_gesture_id = 5;</code>
   */
  public com.imprexion.service.tracking.bean.hand_gesture getHandGestureId() {
    com.imprexion.service.tracking.bean.hand_gesture result = com.imprexion.service.tracking.bean.hand_gesture.valueOf(handGestureId_);
    return result == null ? com.imprexion.service.tracking.bean.hand_gesture.UNRECOGNIZED : result;
  }

  private byte memoizedIsInitialized = -1;
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (validHand_ != 0) {
      output.writeInt32(1, validHand_);
    }
    if (dynamicGestureId_ != com.imprexion.service.tracking.bean.dynamic_gesture.no_dynamic_gesture.getNumber()) {
      output.writeEnum(2, dynamicGestureId_);
    }
    if (leftArmPostureId_ != com.imprexion.service.tracking.bean.arm_posture.no_arm_posture.getNumber()) {
      output.writeEnum(3, leftArmPostureId_);
    }
    if (rightArmPostureId_ != com.imprexion.service.tracking.bean.arm_posture.no_arm_posture.getNumber()) {
      output.writeEnum(4, rightArmPostureId_);
    }
    if (handGestureId_ != com.imprexion.service.tracking.bean.hand_gesture.none.getNumber()) {
      output.writeEnum(5, handGestureId_);
    }
    unknownFields.writeTo(output);
  }

  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (validHand_ != 0) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt32Size(1, validHand_);
    }
    if (dynamicGestureId_ != com.imprexion.service.tracking.bean.dynamic_gesture.no_dynamic_gesture.getNumber()) {
      size += com.google.protobuf.CodedOutputStream
        .computeEnumSize(2, dynamicGestureId_);
    }
    if (leftArmPostureId_ != com.imprexion.service.tracking.bean.arm_posture.no_arm_posture.getNumber()) {
      size += com.google.protobuf.CodedOutputStream
        .computeEnumSize(3, leftArmPostureId_);
    }
    if (rightArmPostureId_ != com.imprexion.service.tracking.bean.arm_posture.no_arm_posture.getNumber()) {
      size += com.google.protobuf.CodedOutputStream
        .computeEnumSize(4, rightArmPostureId_);
    }
    if (handGestureId_ != com.imprexion.service.tracking.bean.hand_gesture.none.getNumber()) {
      size += com.google.protobuf.CodedOutputStream
        .computeEnumSize(5, handGestureId_);
    }
    size += unknownFields.getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof com.imprexion.service.tracking.bean.user_info)) {
      return super.equals(obj);
    }
    com.imprexion.service.tracking.bean.user_info other = (com.imprexion.service.tracking.bean.user_info) obj;

    boolean result = true;
    result = result && (getValidHand()
        == other.getValidHand());
    result = result && dynamicGestureId_ == other.dynamicGestureId_;
    result = result && leftArmPostureId_ == other.leftArmPostureId_;
    result = result && rightArmPostureId_ == other.rightArmPostureId_;
    result = result && handGestureId_ == other.handGestureId_;
    result = result && unknownFields.equals(other.unknownFields);
    return result;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    hash = (37 * hash) + VALID_HAND_FIELD_NUMBER;
    hash = (53 * hash) + getValidHand();
    hash = (37 * hash) + DYNAMIC_GESTURE_ID_FIELD_NUMBER;
    hash = (53 * hash) + dynamicGestureId_;
    hash = (37 * hash) + LEFT_ARM_POSTURE_ID_FIELD_NUMBER;
    hash = (53 * hash) + leftArmPostureId_;
    hash = (37 * hash) + RIGHT_ARM_POSTURE_ID_FIELD_NUMBER;
    hash = (53 * hash) + rightArmPostureId_;
    hash = (37 * hash) + HAND_GESTURE_ID_FIELD_NUMBER;
    hash = (53 * hash) + handGestureId_;
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.imprexion.service.tracking.bean.user_info parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.imprexion.service.tracking.bean.user_info parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.imprexion.service.tracking.bean.user_info parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.imprexion.service.tracking.bean.user_info parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.imprexion.service.tracking.bean.user_info parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.imprexion.service.tracking.bean.user_info parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.imprexion.service.tracking.bean.user_info parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.imprexion.service.tracking.bean.user_info parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.imprexion.service.tracking.bean.user_info parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static com.imprexion.service.tracking.bean.user_info parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.imprexion.service.tracking.bean.user_info parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.imprexion.service.tracking.bean.user_info parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(com.imprexion.service.tracking.bean.user_info prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code tracking.user_info}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:tracking.user_info)
      com.imprexion.service.tracking.bean.user_infoOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.imprexion.service.tracking.bean.TrackingMessage.internal_static_tracking_user_info_descriptor;
    }

    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.imprexion.service.tracking.bean.TrackingMessage.internal_static_tracking_user_info_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.imprexion.service.tracking.bean.user_info.class, com.imprexion.service.tracking.bean.user_info.Builder.class);
    }

    // Construct using com.imprexion.service.tracking.bean.user_info.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3
              .alwaysUseFieldBuilders) {
      }
    }
    public Builder clear() {
      super.clear();
      validHand_ = 0;

      dynamicGestureId_ = 0;

      leftArmPostureId_ = 0;

      rightArmPostureId_ = 0;

      handGestureId_ = 0;

      return this;
    }

    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.imprexion.service.tracking.bean.TrackingMessage.internal_static_tracking_user_info_descriptor;
    }

    public com.imprexion.service.tracking.bean.user_info getDefaultInstanceForType() {
      return com.imprexion.service.tracking.bean.user_info.getDefaultInstance();
    }

    public com.imprexion.service.tracking.bean.user_info build() {
      com.imprexion.service.tracking.bean.user_info result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    public com.imprexion.service.tracking.bean.user_info buildPartial() {
      com.imprexion.service.tracking.bean.user_info result = new com.imprexion.service.tracking.bean.user_info(this);
      result.validHand_ = validHand_;
      result.dynamicGestureId_ = dynamicGestureId_;
      result.leftArmPostureId_ = leftArmPostureId_;
      result.rightArmPostureId_ = rightArmPostureId_;
      result.handGestureId_ = handGestureId_;
      onBuilt();
      return result;
    }

    public Builder clone() {
      return (Builder) super.clone();
    }
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return (Builder) super.setField(field, value);
    }
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return (Builder) super.clearField(field);
    }
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return (Builder) super.clearOneof(oneof);
    }
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, java.lang.Object value) {
      return (Builder) super.setRepeatedField(field, index, value);
    }
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return (Builder) super.addRepeatedField(field, value);
    }
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof com.imprexion.service.tracking.bean.user_info) {
        return mergeFrom((com.imprexion.service.tracking.bean.user_info)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.imprexion.service.tracking.bean.user_info other) {
      if (other == com.imprexion.service.tracking.bean.user_info.getDefaultInstance()) return this;
      if (other.getValidHand() != 0) {
        setValidHand(other.getValidHand());
      }
      if (other.dynamicGestureId_ != 0) {
        setDynamicGestureIdValue(other.getDynamicGestureIdValue());
      }
      if (other.leftArmPostureId_ != 0) {
        setLeftArmPostureIdValue(other.getLeftArmPostureIdValue());
      }
      if (other.rightArmPostureId_ != 0) {
        setRightArmPostureIdValue(other.getRightArmPostureIdValue());
      }
      if (other.handGestureId_ != 0) {
        setHandGestureIdValue(other.getHandGestureIdValue());
      }
      this.mergeUnknownFields(other.unknownFields);
      onChanged();
      return this;
    }

    public final boolean isInitialized() {
      return true;
    }

    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      com.imprexion.service.tracking.bean.user_info parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (com.imprexion.service.tracking.bean.user_info) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private int validHand_ ;
    /**
     * <code>int32 valid_hand = 1;</code>
     */
    public int getValidHand() {
      return validHand_;
    }
    /**
     * <code>int32 valid_hand = 1;</code>
     */
    public Builder setValidHand(int value) {
      
      validHand_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>int32 valid_hand = 1;</code>
     */
    public Builder clearValidHand() {
      
      validHand_ = 0;
      onChanged();
      return this;
    }

    private int dynamicGestureId_ = 0;
    /**
     * <code>.tracking.dynamic_gesture dynamic_gesture_id = 2;</code>
     */
    public int getDynamicGestureIdValue() {
      return dynamicGestureId_;
    }
    /**
     * <code>.tracking.dynamic_gesture dynamic_gesture_id = 2;</code>
     */
    public Builder setDynamicGestureIdValue(int value) {
      dynamicGestureId_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>.tracking.dynamic_gesture dynamic_gesture_id = 2;</code>
     */
    public com.imprexion.service.tracking.bean.dynamic_gesture getDynamicGestureId() {
      com.imprexion.service.tracking.bean.dynamic_gesture result = com.imprexion.service.tracking.bean.dynamic_gesture.valueOf(dynamicGestureId_);
      return result == null ? com.imprexion.service.tracking.bean.dynamic_gesture.UNRECOGNIZED : result;
    }
    /**
     * <code>.tracking.dynamic_gesture dynamic_gesture_id = 2;</code>
     */
    public Builder setDynamicGestureId(com.imprexion.service.tracking.bean.dynamic_gesture value) {
      if (value == null) {
        throw new NullPointerException();
      }
      
      dynamicGestureId_ = value.getNumber();
      onChanged();
      return this;
    }
    /**
     * <code>.tracking.dynamic_gesture dynamic_gesture_id = 2;</code>
     */
    public Builder clearDynamicGestureId() {
      
      dynamicGestureId_ = 0;
      onChanged();
      return this;
    }

    private int leftArmPostureId_ = 0;
    /**
     * <code>.tracking.arm_posture left_arm_posture_id = 3;</code>
     */
    public int getLeftArmPostureIdValue() {
      return leftArmPostureId_;
    }
    /**
     * <code>.tracking.arm_posture left_arm_posture_id = 3;</code>
     */
    public Builder setLeftArmPostureIdValue(int value) {
      leftArmPostureId_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>.tracking.arm_posture left_arm_posture_id = 3;</code>
     */
    public com.imprexion.service.tracking.bean.arm_posture getLeftArmPostureId() {
      com.imprexion.service.tracking.bean.arm_posture result = com.imprexion.service.tracking.bean.arm_posture.valueOf(leftArmPostureId_);
      return result == null ? com.imprexion.service.tracking.bean.arm_posture.UNRECOGNIZED : result;
    }
    /**
     * <code>.tracking.arm_posture left_arm_posture_id = 3;</code>
     */
    public Builder setLeftArmPostureId(com.imprexion.service.tracking.bean.arm_posture value) {
      if (value == null) {
        throw new NullPointerException();
      }
      
      leftArmPostureId_ = value.getNumber();
      onChanged();
      return this;
    }
    /**
     * <code>.tracking.arm_posture left_arm_posture_id = 3;</code>
     */
    public Builder clearLeftArmPostureId() {
      
      leftArmPostureId_ = 0;
      onChanged();
      return this;
    }

    private int rightArmPostureId_ = 0;
    /**
     * <code>.tracking.arm_posture right_arm_posture_id = 4;</code>
     */
    public int getRightArmPostureIdValue() {
      return rightArmPostureId_;
    }
    /**
     * <code>.tracking.arm_posture right_arm_posture_id = 4;</code>
     */
    public Builder setRightArmPostureIdValue(int value) {
      rightArmPostureId_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>.tracking.arm_posture right_arm_posture_id = 4;</code>
     */
    public com.imprexion.service.tracking.bean.arm_posture getRightArmPostureId() {
      com.imprexion.service.tracking.bean.arm_posture result = com.imprexion.service.tracking.bean.arm_posture.valueOf(rightArmPostureId_);
      return result == null ? com.imprexion.service.tracking.bean.arm_posture.UNRECOGNIZED : result;
    }
    /**
     * <code>.tracking.arm_posture right_arm_posture_id = 4;</code>
     */
    public Builder setRightArmPostureId(com.imprexion.service.tracking.bean.arm_posture value) {
      if (value == null) {
        throw new NullPointerException();
      }
      
      rightArmPostureId_ = value.getNumber();
      onChanged();
      return this;
    }
    /**
     * <code>.tracking.arm_posture right_arm_posture_id = 4;</code>
     */
    public Builder clearRightArmPostureId() {
      
      rightArmPostureId_ = 0;
      onChanged();
      return this;
    }

    private int handGestureId_ = 0;
    /**
     * <code>.tracking.hand_gesture hand_gesture_id = 5;</code>
     */
    public int getHandGestureIdValue() {
      return handGestureId_;
    }
    /**
     * <code>.tracking.hand_gesture hand_gesture_id = 5;</code>
     */
    public Builder setHandGestureIdValue(int value) {
      handGestureId_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>.tracking.hand_gesture hand_gesture_id = 5;</code>
     */
    public com.imprexion.service.tracking.bean.hand_gesture getHandGestureId() {
      com.imprexion.service.tracking.bean.hand_gesture result = com.imprexion.service.tracking.bean.hand_gesture.valueOf(handGestureId_);
      return result == null ? com.imprexion.service.tracking.bean.hand_gesture.UNRECOGNIZED : result;
    }
    /**
     * <code>.tracking.hand_gesture hand_gesture_id = 5;</code>
     */
    public Builder setHandGestureId(com.imprexion.service.tracking.bean.hand_gesture value) {
      if (value == null) {
        throw new NullPointerException();
      }
      
      handGestureId_ = value.getNumber();
      onChanged();
      return this;
    }
    /**
     * <code>.tracking.hand_gesture hand_gesture_id = 5;</code>
     */
    public Builder clearHandGestureId() {
      
      handGestureId_ = 0;
      onChanged();
      return this;
    }
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFieldsProto3(unknownFields);
    }

    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:tracking.user_info)
  }

  // @@protoc_insertion_point(class_scope:tracking.user_info)
  private static final com.imprexion.service.tracking.bean.user_info DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.imprexion.service.tracking.bean.user_info();
  }

  public static com.imprexion.service.tracking.bean.user_info getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<user_info>
      PARSER = new com.google.protobuf.AbstractParser<user_info>() {
    public user_info parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new user_info(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<user_info> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<user_info> getParserForType() {
    return PARSER;
  }

  public com.imprexion.service.tracking.bean.user_info getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

