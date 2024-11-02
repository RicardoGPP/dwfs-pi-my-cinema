<template>
    <div class="session-panel">
        <h1>
            Sessões
        </h1>
        <hr/>
        <LifecyclePanel :callback="load">
            <div v-if="sessions.length === 0" class="message">
                Não há sessões disponíveis para este filme.
            </div>
            <div v-else class="content">
                <div class="row">
                    <h3>
                        Unidade:
                    </h3>
                    <SelectButton
                        v-model="location"
                        :options="locationOptions"
                        optionLabel="label"
                        optionValue="value"
                    />
                </div>
                <div v-if="location" class="row">
                    <h3>
                        Dia:
                    </h3>
                    <SelectButton
                        v-model="date"
                        :options="dateOptions"
                        optionLabel="label"
                        optionValue="value"
                    />
                </div>
                <div v-if="date" class="row">
                    <h3>
                        Horário:
                    </h3>
                    <SelectButton
                        v-model="time"
                        :options="timeOptions"
                        optionLabel="label"
                        optionValue="value"
                    />
                </div>
                <InfoPanel
                    v-if="session"
                    title="Detalhes da sessão"
                    :message="`Nesta sessão o filme será em ${twoOrThreeD} e ${dubbedOrSubtitled}.`"
                />
            </div>
        </LifecyclePanel>
    </div>
</template>

<script>
import SelectButton from 'primevue/selectbutton';
import LifecyclePanel from '@/components/lifecycle-panel/LifecyclePanel.vue';
import InfoPanel from '@/components/info-panel/InfoPanel.vue';
import SessionService from '@/services/session-service';

export default {
    name: 'SessionPanel',
    components: {
        SelectButton,
        LifecyclePanel,
        InfoPanel
    },
    props: {
        movie: {
            type: Object,
            required: true
        }
    },
    data() {
        return {
            sessions: [],
            location: null,
            date: null,
            time: null
        }
    },
    computed: {
        locationOptions() {
            const locations = [];

            for (let session of this.sessions) {
                const location = session.location;

                if (locations.find((other) => other.id === location.id)) {
                    continue;
                }

                locations.push(location);
            }

            const toOption = (location) => {
                return {
                    label: location.name,
                    value: location
                }
            };

            return locations.map(toOption);
        },
        dateOptions() {
            if (!this.location) {
                return [];
            }

            const dates = [];

            for (let session of this.sessions) {
                const location = session.location;

                if (location.id !== this.location.id) {
                    continue;
                }

                const date = session.date;

                if (dates.includes(date)) {
                    continue;
                }

                dates.push(date);
            }

            const toOption = (date) => {
                const parts = date.split('-');

                return {
                    label: `${parts[2]}/${parts[1]}`,
                    value: date
                }
            };

            return dates.map(toOption);
        },
        timeOptions() {
            if (!this.location || !this.date) {
                return [];
            }

            const times = [];

            for (let session of this.sessions) {
                const location = session.location;

                if (location.id !== this.location.id) {
                    continue;
                }

                const date = session.date;

                if (date !== this.date) {
                    continue;
                }

                const time = session.time;

                if (times.includes(time)) {
                    continue;
                }

                times.push(time);
            }

            const toOption = (time) => {
                const parts = time.split(':');

                return {
                    label: `${parts[0]}:${parts[1]}`,
                    value: time
                }
            };

            return times.map(toOption);
        },
        session() {
            const location = this.location;

            if (!location) {
                return null;
            }

            const date = this.date;

            if (!date) {
                return null;
            }

            const time = this.time;

            if (!time) {
                return null;
            }

            for (let session of this.sessions) {
                if (session.location.id !== location.id) {
                    continue;
                }

                if (session.date !== date) {
                    continue;
                }

                if (session.time !== time) {
                    continue;
                }

                return session;
            }

            return null;
        },
        twoOrThreeD() {
            if (!this.session) {
                return null;
            }

            if (this.session.threeD) {
                return '3D';
            }

            return '2D';
        },
        dubbedOrSubtitled() {
            if (!this.session) {
                return null;
            }

            if (this.session.subtitled) {
                return 'legendado';
            }

            return 'dublado';
        }
    },
    watch: {
        'locationOptions'(newValue) {
            let location = null;

            if (newValue.length > 0) {
                location = newValue[0].value;
            }

            this.location = location;
        },
        'location'(newValue) {
            let date = null;

            if (newValue) {
                date = this.dateOptions[0].value;
            }

            this.date = date;
        },
        'date'(newValue) {
            let time = null;

            if (newValue) {
                time = this.timeOptions[0].value;
            }

            this.time = time;
        }
    },
    methods: {
        reset() {
            this.sessions = [];
            this.location = null;
            this.date = null;
            this.time = null;
        },
        async load() {
            this.reset();
            this.sessions = await SessionService.getAll(this.movie.id);
        }
    }
}
</script>

<style lang="scss" scoped>
.content {
    display: flex;
    flex-direction: column;
    gap: 20px;
}

.row {
    display: flex;
    flex-direction: column;
    gap: 10px;
}

hr {
    margin-bottom: 30px;
}

h3 {
    margin: 0;
}
</style>