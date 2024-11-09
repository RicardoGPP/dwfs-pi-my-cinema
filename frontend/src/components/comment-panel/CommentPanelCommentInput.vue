<template>
    <div class="comment-panel-comment-input">
        <Textarea
            v-model="text"
            rows="5"
            cols="30"
            autoResize
        />
        <span class="message" :invalid="leftChars < 0">
            {{ leftChars }} caractere(s) restante(s).
        </span>
        <div>
            <Button
                label="Postar"
                :disabled="!canPost"
                @click="post"
            />
        </div>
    </div>
</template>

<script>
import Textarea from 'primevue/textarea';
import Button from 'primevue/button';
import _ from 'lodash';

export default {
    name: 'CommentPanelCommentInput',
    components: {
        Textarea,
        Button
    },
    data() {
        return {
            text: ''
        }
    },
    computed: {
        canPost() {
            return !_.isEmpty(this.text) && this.leftChars >= 0;
        },
        leftChars() {
            return 150 - this.text?.length;
        }
    },
    methods: {
        post() {
            this.$emit('post', this.text);
            this.text = '';
        }
    }
}
</script>

<style lang="scss" scoped>
.comment-panel-comment-input {
    display: flex;
    flex-direction: column;
    gap: 10px;

    & > div {
        display: flex;
        justify-content: flex-end;
    }
}

.message {
    color: #808080;
    font-size: 12px;

    &[invalid = true] {
        color: #ff0000;
    }
}
</style>