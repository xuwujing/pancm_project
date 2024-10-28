// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: AsynReqVo.proto

package com.zans.vo;

public final class AsynReqVo {
  private AsynReqVo() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  public interface AsynReqVoMsgOrBuilder extends
      // @@protoc_insertion_point(interface_extends:AsynReqVoMsg)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <pre>
     * ID
     * </pre>
     *
     * <code>optional int32 state = 1;</code>
     */
    int getState();

    /**
     * <pre>
     * taskId
     * </pre>
     *
     * <code>optional string taskId = 2;</code>
     */
    String getTaskId();
    /**
     * <pre>
     * taskId
     * </pre>
     *
     * <code>optional string taskId = 2;</code>
     */
    com.google.protobuf.ByteString
        getTaskIdBytes();

    /**
     * <pre>
     * 名称
     * </pre>
     *
     * <code>optional string name = 3;</code>
     */
    String getName();
    /**
     * <pre>
     * 名称
     * </pre>
     *
     * <code>optional string name = 3;</code>
     */
    com.google.protobuf.ByteString
        getNameBytes();

    /**
     * <pre>
     * 状态
     * </pre>
     *
     * <code>optional string data = 4;</code>
     */
    String getData();
    /**
     * <pre>
     * 状态
     * </pre>
     *
     * <code>optional string data = 4;</code>
     */
    com.google.protobuf.ByteString
        getDataBytes();
  }
  /**
   * Protobuf type {@code AsynReqVoMsg}
   */
  public  static final class AsynReqVoMsg extends
      com.google.protobuf.GeneratedMessageV3 implements
      // @@protoc_insertion_point(message_implements:AsynReqVoMsg)
      AsynReqVoMsgOrBuilder {
    // Use AsynReqVoMsg.newBuilder() to construct.
    private AsynReqVoMsg(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private AsynReqVoMsg() {
      state_ = 0;
      taskId_ = "";
      name_ = "";
      data_ = "";
    }

    @Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return com.google.protobuf.UnknownFieldSet.getDefaultInstance();
    }
    private AsynReqVoMsg(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      this();
      int mutable_bitField0_ = 0;
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!input.skipField(tag)) {
                done = true;
              }
              break;
            }
            case 8: {

              state_ = input.readInt32();
              break;
            }
            case 18: {
              String s = input.readStringRequireUtf8();

              taskId_ = s;
              break;
            }
            case 26: {
              String s = input.readStringRequireUtf8();

              name_ = s;
              break;
            }
            case 34: {
              String s = input.readStringRequireUtf8();

              data_ = s;
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
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return AsynReqVo.internal_static_AsynReqVoMsg_descriptor;
    }

    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return AsynReqVo.internal_static_AsynReqVoMsg_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              AsynReqVoMsg.class, Builder.class);
    }

    public static final int STATE_FIELD_NUMBER = 1;
    private int state_;
    /**
     * <pre>
     * ID
     * </pre>
     *
     * <code>optional int32 state = 1;</code>
     */
    public int getState() {
      return state_;
    }

    public static final int TASKID_FIELD_NUMBER = 2;
    private volatile Object taskId_;
    /**
     * <pre>
     * taskId
     * </pre>
     *
     * <code>optional string taskId = 2;</code>
     */
    public String getTaskId() {
      Object ref = taskId_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        taskId_ = s;
        return s;
      }
    }
    /**
     * <pre>
     * taskId
     * </pre>
     *
     * <code>optional string taskId = 2;</code>
     */
    public com.google.protobuf.ByteString
        getTaskIdBytes() {
      Object ref = taskId_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b =
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        taskId_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int NAME_FIELD_NUMBER = 3;
    private volatile Object name_;
    /**
     * <pre>
     * 名称
     * </pre>
     *
     * <code>optional string name = 3;</code>
     */
    public String getName() {
      Object ref = name_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        name_ = s;
        return s;
      }
    }
    /**
     * <pre>
     * 名称
     * </pre>
     *
     * <code>optional string name = 3;</code>
     */
    public com.google.protobuf.ByteString
        getNameBytes() {
      Object ref = name_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b =
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        name_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int DATA_FIELD_NUMBER = 4;
    private volatile Object data_;
    /**
     * <pre>
     * 状态
     * </pre>
     *
     * <code>optional string data = 4;</code>
     */
    public String getData() {
      Object ref = data_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        data_ = s;
        return s;
      }
    }
    /**
     * <pre>
     * 状态
     * </pre>
     *
     * <code>optional string data = 4;</code>
     */
    public com.google.protobuf.ByteString
        getDataBytes() {
      Object ref = data_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b =
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        data_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
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
      if (state_ != 0) {
        output.writeInt32(1, state_);
      }
      if (!getTaskIdBytes().isEmpty()) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 2, taskId_);
      }
      if (!getNameBytes().isEmpty()) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 3, name_);
      }
      if (!getDataBytes().isEmpty()) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 4, data_);
      }
    }

    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (state_ != 0) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(1, state_);
      }
      if (!getTaskIdBytes().isEmpty()) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(2, taskId_);
      }
      if (!getNameBytes().isEmpty()) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(3, name_);
      }
      if (!getDataBytes().isEmpty()) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(4, data_);
      }
      memoizedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @Override
    public boolean equals(final Object obj) {
      if (obj == this) {
       return true;
      }
      if (!(obj instanceof AsynReqVoMsg)) {
        return super.equals(obj);
      }
      AsynReqVoMsg other = (AsynReqVoMsg) obj;

      boolean result = true;
      result = result && (getState()
          == other.getState());
      result = result && getTaskId()
          .equals(other.getTaskId());
      result = result && getName()
          .equals(other.getName());
      result = result && getData()
          .equals(other.getData());
      return result;
    }

    @Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptorForType().hashCode();
      hash = (37 * hash) + STATE_FIELD_NUMBER;
      hash = (53 * hash) + getState();
      hash = (37 * hash) + TASKID_FIELD_NUMBER;
      hash = (53 * hash) + getTaskId().hashCode();
      hash = (37 * hash) + NAME_FIELD_NUMBER;
      hash = (53 * hash) + getName().hashCode();
      hash = (37 * hash) + DATA_FIELD_NUMBER;
      hash = (53 * hash) + getData().hashCode();
      hash = (29 * hash) + unknownFields.hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static AsynReqVoMsg parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static AsynReqVoMsg parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static AsynReqVoMsg parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static AsynReqVoMsg parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static AsynReqVoMsg parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static AsynReqVoMsg parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }
    public static AsynReqVoMsg parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }
    public static AsynReqVoMsg parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static AsynReqVoMsg parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static AsynReqVoMsg parseFrom(
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
    public static Builder newBuilder(AsynReqVoMsg prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
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
     * Protobuf type {@code AsynReqVoMsg}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:AsynReqVoMsg)
        AsynReqVoMsgOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return AsynReqVo.internal_static_AsynReqVoMsg_descriptor;
      }

      protected FieldAccessorTable
          internalGetFieldAccessorTable() {
        return AsynReqVo.internal_static_AsynReqVoMsg_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                AsynReqVoMsg.class, Builder.class);
      }

      // Construct using com.zans.vo.AsynReqVo.AsynReqVoMsg.newBuilder()
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
      public Builder clear() {
        super.clear();
        state_ = 0;

        taskId_ = "";

        name_ = "";

        data_ = "";

        return this;
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return AsynReqVo.internal_static_AsynReqVoMsg_descriptor;
      }

      public AsynReqVoMsg getDefaultInstanceForType() {
        return AsynReqVoMsg.getDefaultInstance();
      }

      public AsynReqVoMsg build() {
        AsynReqVoMsg result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public AsynReqVoMsg buildPartial() {
        AsynReqVoMsg result = new AsynReqVoMsg(this);
        result.state_ = state_;
        result.taskId_ = taskId_;
        result.name_ = name_;
        result.data_ = data_;
        onBuilt();
        return result;
      }

      public Builder clone() {
        return (Builder) super.clone();
      }
      public Builder setField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
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
          int index, Object value) {
        return (Builder) super.setRepeatedField(field, index, value);
      }
      public Builder addRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return (Builder) super.addRepeatedField(field, value);
      }
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof AsynReqVoMsg) {
          return mergeFrom((AsynReqVoMsg)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(AsynReqVoMsg other) {
        if (other == AsynReqVoMsg.getDefaultInstance()) return this;
        if (other.getState() != 0) {
          setState(other.getState());
        }
        if (!other.getTaskId().isEmpty()) {
          taskId_ = other.taskId_;
          onChanged();
        }
        if (!other.getName().isEmpty()) {
          name_ = other.name_;
          onChanged();
        }
        if (!other.getData().isEmpty()) {
          data_ = other.data_;
          onChanged();
        }
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
        AsynReqVoMsg parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (AsynReqVoMsg) e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }

      private int state_ ;
      /**
       * <pre>
       * ID
       * </pre>
       *
       * <code>optional int32 state = 1;</code>
       */
      public int getState() {
        return state_;
      }
      /**
       * <pre>
       * ID
       * </pre>
       *
       * <code>optional int32 state = 1;</code>
       */
      public Builder setState(int value) {

        state_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       * ID
       * </pre>
       *
       * <code>optional int32 state = 1;</code>
       */
      public Builder clearState() {

        state_ = 0;
        onChanged();
        return this;
      }

      private Object taskId_ = "";
      /**
       * <pre>
       * taskId
       * </pre>
       *
       * <code>optional string taskId = 2;</code>
       */
      public String getTaskId() {
        Object ref = taskId_;
        if (!(ref instanceof String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          String s = bs.toStringUtf8();
          taskId_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      /**
       * <pre>
       * taskId
       * </pre>
       *
       * <code>optional string taskId = 2;</code>
       */
      public com.google.protobuf.ByteString
          getTaskIdBytes() {
        Object ref = taskId_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b =
              com.google.protobuf.ByteString.copyFromUtf8(
                  (String) ref);
          taskId_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <pre>
       * taskId
       * </pre>
       *
       * <code>optional string taskId = 2;</code>
       */
      public Builder setTaskId(
          String value) {
        if (value == null) {
    throw new NullPointerException();
  }

        taskId_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       * taskId
       * </pre>
       *
       * <code>optional string taskId = 2;</code>
       */
      public Builder clearTaskId() {

        taskId_ = getDefaultInstance().getTaskId();
        onChanged();
        return this;
      }
      /**
       * <pre>
       * taskId
       * </pre>
       *
       * <code>optional string taskId = 2;</code>
       */
      public Builder setTaskIdBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);

        taskId_ = value;
        onChanged();
        return this;
      }

      private Object name_ = "";
      /**
       * <pre>
       * 名称
       * </pre>
       *
       * <code>optional string name = 3;</code>
       */
      public String getName() {
        Object ref = name_;
        if (!(ref instanceof String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          String s = bs.toStringUtf8();
          name_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      /**
       * <pre>
       * 名称
       * </pre>
       *
       * <code>optional string name = 3;</code>
       */
      public com.google.protobuf.ByteString
          getNameBytes() {
        Object ref = name_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b =
              com.google.protobuf.ByteString.copyFromUtf8(
                  (String) ref);
          name_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <pre>
       * 名称
       * </pre>
       *
       * <code>optional string name = 3;</code>
       */
      public Builder setName(
          String value) {
        if (value == null) {
    throw new NullPointerException();
  }

        name_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       * 名称
       * </pre>
       *
       * <code>optional string name = 3;</code>
       */
      public Builder clearName() {

        name_ = getDefaultInstance().getName();
        onChanged();
        return this;
      }
      /**
       * <pre>
       * 名称
       * </pre>
       *
       * <code>optional string name = 3;</code>
       */
      public Builder setNameBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);

        name_ = value;
        onChanged();
        return this;
      }

      private Object data_ = "";
      /**
       * <pre>
       * 状态
       * </pre>
       *
       * <code>optional string data = 4;</code>
       */
      public String getData() {
        Object ref = data_;
        if (!(ref instanceof String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          String s = bs.toStringUtf8();
          data_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      /**
       * <pre>
       * 状态
       * </pre>
       *
       * <code>optional string data = 4;</code>
       */
      public com.google.protobuf.ByteString
          getDataBytes() {
        Object ref = data_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b =
              com.google.protobuf.ByteString.copyFromUtf8(
                  (String) ref);
          data_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <pre>
       * 状态
       * </pre>
       *
       * <code>optional string data = 4;</code>
       */
      public Builder setData(
          String value) {
        if (value == null) {
    throw new NullPointerException();
  }

        data_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       * 状态
       * </pre>
       *
       * <code>optional string data = 4;</code>
       */
      public Builder clearData() {

        data_ = getDefaultInstance().getData();
        onChanged();
        return this;
      }
      /**
       * <pre>
       * 状态
       * </pre>
       *
       * <code>optional string data = 4;</code>
       */
      public Builder setDataBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);

        data_ = value;
        onChanged();
        return this;
      }
      public final Builder setUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return this;
      }

      public final Builder mergeUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return this;
      }


      // @@protoc_insertion_point(builder_scope:AsynReqVoMsg)
    }

    // @@protoc_insertion_point(class_scope:AsynReqVoMsg)
    private static final AsynReqVoMsg DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new AsynReqVoMsg();
    }

    public static AsynReqVoMsg getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<AsynReqVoMsg>
        PARSER = new com.google.protobuf.AbstractParser<AsynReqVoMsg>() {
      public AsynReqVoMsg parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
          return new AsynReqVoMsg(input, extensionRegistry);
      }
    };

    public static com.google.protobuf.Parser<AsynReqVoMsg> parser() {
      return PARSER;
    }

    @Override
    public com.google.protobuf.Parser<AsynReqVoMsg> getParserForType() {
      return PARSER;
    }

    public AsynReqVoMsg getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_AsynReqVoMsg_descriptor;
  private static final
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_AsynReqVoMsg_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    String[] descriptorData = {
      "\n\017AsynReqVo.proto\"I\n\014AsynReqVoMsg\022\r\n\005sta" +
      "te\030\001 \001(\005\022\016\n\006taskId\030\002 \001(\t\022\014\n\004name\030\003 \001(\t\022\014" +
      "\n\004data\030\004 \001(\tB\030\n\013com.zans.voB\tAsynReqVob\006" +
      "proto3"
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
    internal_static_AsynReqVoMsg_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_AsynReqVoMsg_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_AsynReqVoMsg_descriptor,
        new String[] { "State", "TaskId", "Name", "Data", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}