package services;

public final class ParkedCarAttributes {
    private final int slot;
    private final String registrationNumber;
    private final String color;

    private ParkedCarAttributes(Builder builder) {
        slot = builder.slot;
        registrationNumber = builder.registrationNumber;
        color = builder.color;
    }

    public int getSlot() {
        return slot;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getColor() {
        return color;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParkedCarAttributes that = (ParkedCarAttributes) o;

        if (slot != that.slot) return false;
        if (!registrationNumber.equals(that.registrationNumber)) return false;
        return color.equals(that.color);
    }

    @Override
    public int hashCode() {
        int result = slot;
        result = 31 * result + registrationNumber.hashCode();
        result = 31 * result + color.hashCode();
        return result;
    }

    public static final class Builder {
        private int slot;
        private String registrationNumber;
        private String color;

        private Builder() {
        }

        public Builder withSlot(int slot) {
            this.slot = slot;
            return this;
        }

        public Builder withRegistrationNumber(String registrationNumber) {
            this.registrationNumber = registrationNumber;
            return this;
        }

        public Builder withColor(String color) {
            this.color = color;
            return this;
        }

        public ParkedCarAttributes build() {
            return new ParkedCarAttributes(this);
        }
    }
}
