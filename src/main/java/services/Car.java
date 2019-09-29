package services;

public final class Car {
    private final String registration;
    private final String color;

    public String getRegistration() {
        return registration;
    }

    public String getColor() {
        return color;
    }

    private Car(Builder builder) {
        registration = builder.registration;
        color = builder.color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Car car = (Car) o;

        if (!registration.equals(car.registration)) return false;
        return color.equals(car.color);
    }

    @Override
    public int hashCode() {
        int result = registration.hashCode();
        result = 31 * result + color.hashCode();
        return result;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private String registration;
        private String color;

        private Builder() {
        }

        public Builder withRegistration(String registration) {
            this.registration = registration;
            return this;
        }

        public Builder withColor(String color) {
            this.color = color;
            return this;
        }

        public Car build() {
            return new Car(this);
        }
    }
}
