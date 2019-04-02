package com.imprexion.aiscreen.bean;// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: MessageForAIScreen.proto

public final class MessageForAIScreenPB {
  private MessageForAIScreenPB() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  public interface MessageForAIScreenOrBuilder extends
      // @@protoc_insertion_point(interface_extends:MessageForAIScreen)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>int32 usrsex = 1;</code>
     */
    int getUsrsex();

    /**
     * <code>bool standHere = 3;</code>
     */
    boolean getStandHere();

    /**
     * <code>bool isActived = 2;</code>
     */
    boolean getIsActived();
  }
  /**
   * Protobuf type {@code MessageForAIScreen}
   */
  public  static final class MessageForAIScreen extends
      com.google.protobuf.GeneratedMessageV3 implements
      // @@protoc_insertion_point(message_implements:MessageForAIScreen)
      MessageForAIScreenOrBuilder {
  private static final long serialVersionUID = 0L;
    // Use MessageForAIScreen.newBuilder() to construct.
    private MessageForAIScreen(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private MessageForAIScreen() {
    }

    @Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return this.unknownFields;
    }
    private MessageForAIScreen(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      this();
      if (extensionRegistry == null) {
        throw new NullPointerException();
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
            case 8: {

              usrsex_ = input.readInt32();
              break;
            }
            case 16: {

              isActived_ = input.readBool();
              break;
            }
            case 24: {

              standHere_ = input.readBool();
              break;
            }
            default: {
              if (!parseUnknownField(
                  input, unknownFields, extensionRegistry, tag)) {
                done = true;
              }
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
      return MessageForAIScreenPB.internal_static_MessageForAIScreen_descriptor;
    }

    @Override
    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return MessageForAIScreenPB.internal_static_MessageForAIScreen_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              MessageForAIScreen.class, Builder.class);
    }

    public static final int USRSEX_FIELD_NUMBER = 1;
    private int usrsex_;
    /**
     * <code>int32 usrsex = 1;</code>
     */
    public int getUsrsex() {
      return usrsex_;
    }

    public static final int STANDHERE_FIELD_NUMBER = 3;
    private boolean standHere_;
    /**
     * <code>bool standHere = 3;</code>
     */
    public boolean getStandHere() {
      return standHere_;
    }

    public static final int ISACTIVED_FIELD_NUMBER = 2;
    private boolean isActived_;
    /**
     * <code>bool isActived = 2;</code>
     */
    public boolean getIsActived() {
      return isActived_;
    }

    private byte memoizedIsInitialized = -1;
    @Override
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      memoizedIsInitialized = 1;
      return true;
    }

    @Override
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      if (usrsex_ != 0) {
        output.writeInt32(1, usrsex_);
      }
      if (isActived_ != false) {
        output.writeBool(2, isActived_);
      }
      if (standHere_ != false) {
        output.writeBool(3, standHere_);
      }
      unknownFields.writeTo(output);
    }

    @Override
    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (usrsex_ != 0) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(1, usrsex_);
      }
      if (isActived_ != false) {
        size += com.google.protobuf.CodedOutputStream
          .computeBoolSize(2, isActived_);
      }
      if (standHere_ != false) {
        size += com.google.protobuf.CodedOutputStream
          .computeBoolSize(3, standHere_);
      }
      size += unknownFields.getSerializedSize();
      memoizedSize = size;
      return size;
    }

    @Override
    public boolean equals(final Object obj) {
      if (obj == this) {
       return true;
      }
      if (!(obj instanceof MessageForAIScreen)) {
        return super.equals(obj);
      }
      MessageForAIScreen other = (MessageForAIScreen) obj;

      if (getUsrsex()
          != other.getUsrsex()) return false;
      if (getStandHere()
          != other.getStandHere()) return false;
      if (getIsActived()
          != other.getIsActived()) return false;
      if (!unknownFields.equals(other.unknownFields)) return false;
      return true;
    }

    @Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptor().hashCode();
      hash = (37 * hash) + USRSEX_FIELD_NUMBER;
      hash = (53 * hash) + getUsrsex();
      hash = (37 * hash) + STANDHERE_FIELD_NUMBER;
      hash = (53 * hash) + com.google.protobuf.Internal.hashBoolean(
          getStandHere());
      hash = (37 * hash) + ISACTIVED_FIELD_NUMBER;
      hash = (53 * hash) + com.google.protobuf.Internal.hashBoolean(
          getIsActived());
      hash = (29 * hash) + unknownFields.hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static MessageForAIScreen parseFrom(
        java.nio.ByteBuffer data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static MessageForAIScreen parseFrom(
        java.nio.ByteBuffer data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static MessageForAIScreen parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static MessageForAIScreen parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static MessageForAIScreen parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static MessageForAIScreen parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static MessageForAIScreen parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static MessageForAIScreen parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }
    public static MessageForAIScreen parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }
    public static MessageForAIScreen parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static MessageForAIScreen parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static MessageForAIScreen parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }

    @Override
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(MessageForAIScreen prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    @Override
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE
          ? new Builder() : new Builder().mergeFrom(this);
    }

    @Override
    protected Builder newBuilderForType(
        BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code MessageForAIScreen}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:MessageForAIScreen)
        MessageForAIScreenOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return MessageForAIScreenPB.internal_static_MessageForAIScreen_descriptor;
      }

      @Override
      protected FieldAccessorTable
          internalGetFieldAccessorTable() {
        return MessageForAIScreenPB.internal_static_MessageForAIScreen_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                MessageForAIScreen.class, Builder.class);
      }

      // Construct using MessageForAIScreenPB.MessageForAIScreen.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessageV3
                .alwaysUseFieldBuilders) {
        }
      }
      @Override
      public Builder clear() {
        super.clear();
        usrsex_ = 0;

        standHere_ = false;

        isActived_ = false;

        return this;
      }

      @Override
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return MessageForAIScreenPB.internal_static_MessageForAIScreen_descriptor;
      }

      @Override
      public MessageForAIScreen getDefaultInstanceForType() {
        return MessageForAIScreen.getDefaultInstance();
      }

      @Override
      public MessageForAIScreen build() {
        MessageForAIScreen result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      @Override
      public MessageForAIScreen buildPartial() {
        MessageForAIScreen result = new MessageForAIScreen(this);
        result.usrsex_ = usrsex_;
        result.standHere_ = standHere_;
        result.isActived_ = isActived_;
        onBuilt();
        return result;
      }

      @Override
      public Builder clone() {
        return super.clone();
      }
      @Override
      public Builder setField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return super.setField(field, value);
      }
      @Override
      public Builder clearField(
          com.google.protobuf.Descriptors.FieldDescriptor field) {
        return super.clearField(field);
      }
      @Override
      public Builder clearOneof(
          com.google.protobuf.Descriptors.OneofDescriptor oneof) {
        return super.clearOneof(oneof);
      }
      @Override
      public Builder setRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          int index, Object value) {
        return super.setRepeatedField(field, index, value);
      }
      @Override
      public Builder addRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return super.addRepeatedField(field, value);
      }
      @Override
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof MessageForAIScreen) {
          return mergeFrom((MessageForAIScreen)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(MessageForAIScreen other) {
        if (other == MessageForAIScreen.getDefaultInstance()) return this;
        if (other.getUsrsex() != 0) {
          setUsrsex(other.getUsrsex());
        }
        if (other.getStandHere() != false) {
          setStandHere(other.getStandHere());
        }
        if (other.getIsActived() != false) {
          setIsActived(other.getIsActived());
        }
        this.mergeUnknownFields(other.unknownFields);
        onChanged();
        return this;
      }

      @Override
      public final boolean isInitialized() {
        return true;
      }

      @Override
      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        MessageForAIScreen parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (MessageForAIScreen) e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }

      private int usrsex_ ;
      /**
       * <code>int32 usrsex = 1;</code>
       */
      public int getUsrsex() {
        return usrsex_;
      }
      /**
       * <code>int32 usrsex = 1;</code>
       */
      public Builder setUsrsex(int value) {
        
        usrsex_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>int32 usrsex = 1;</code>
       */
      public Builder clearUsrsex() {
        
        usrsex_ = 0;
        onChanged();
        return this;
      }

      private boolean standHere_ ;
      /**
       * <code>bool standHere = 3;</code>
       */
      public boolean getStandHere() {
        return standHere_;
      }
      /**
       * <code>bool standHere = 3;</code>
       */
      public Builder setStandHere(boolean value) {
        
        standHere_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>bool standHere = 3;</code>
       */
      public Builder clearStandHere() {
        
        standHere_ = false;
        onChanged();
        return this;
      }

      private boolean isActived_ ;
      /**
       * <code>bool isActived = 2;</code>
       */
      public boolean getIsActived() {
        return isActived_;
      }
      /**
       * <code>bool isActived = 2;</code>
       */
      public Builder setIsActived(boolean value) {
        
        isActived_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>bool isActived = 2;</code>
       */
      public Builder clearIsActived() {
        
        isActived_ = false;
        onChanged();
        return this;
      }
      @Override
      public final Builder setUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.setUnknownFields(unknownFields);
      }

      @Override
      public final Builder mergeUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.mergeUnknownFields(unknownFields);
      }


      // @@protoc_insertion_point(builder_scope:MessageForAIScreen)
    }

    // @@protoc_insertion_point(class_scope:MessageForAIScreen)
    private static final MessageForAIScreen DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new MessageForAIScreen();
    }

    public static MessageForAIScreen getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<MessageForAIScreen>
        PARSER = new com.google.protobuf.AbstractParser<MessageForAIScreen>() {
      @Override
      public MessageForAIScreen parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new MessageForAIScreen(input, extensionRegistry);
      }
    };

    public static com.google.protobuf.Parser<MessageForAIScreen> parser() {
      return PARSER;
    }

    @Override
    public com.google.protobuf.Parser<MessageForAIScreen> getParserForType() {
      return PARSER;
    }

    @Override
    public MessageForAIScreen getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_MessageForAIScreen_descriptor;
  private static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_MessageForAIScreen_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    String[] descriptorData = {
      "\n\030MessageForAIScreen.proto\"J\n\022MessageFor" +
      "AIScreen\022\016\n\006usrsex\030\001 \001(\005\022\021\n\tstandHere\030\003 " +
      "\001(\010\022\021\n\tisActived\030\002 \001(\010B\026B\024MessageForAISc" +
      "reenPBb\006proto3"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
    internal_static_MessageForAIScreen_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_MessageForAIScreen_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_MessageForAIScreen_descriptor,
        new String[] { "Usrsex", "StandHere", "IsActived", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
